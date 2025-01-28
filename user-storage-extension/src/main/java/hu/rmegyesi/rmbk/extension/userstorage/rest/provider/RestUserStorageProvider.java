package hu.rmegyesi.rmbk.extension.userstorage.rest.provider;

import hu.rmegyesi.rmbk.extension.userstorage.rest.api.ApiException;
import hu.rmegyesi.rmbk.extension.userstorage.rest.api.UserResourceApi;
import hu.rmegyesi.rmbk.extension.userstorage.rest.model.AuthenticateUserBody;
import hu.rmegyesi.rmbk.extension.userstorage.rest.model.RestUserModel;
import hu.rmegyesi.rmbk.extension.userstorage.rest.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class RestUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

    private final KeycloakSession session;
    private final RealmModel realm;
    private final ComponentModel storageProviderModel;
    private final UserResourceApi client;

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return false;
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()) || !(credentialInput instanceof UserCredentialModel userCredentialModel)) {
            return false;
        }

        try {
            client.authenticate(
                    new AuthenticateUserBody()
                            .username(user.getUsername())
                            .password(userCredentialModel.getValue())
            );
            return true;
        } catch (ApiException e) {
            if (e.getResponse().getStatus() == 401 || e.getResponse().getStatus() == 404) {
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {

    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        StorageId storageId = new StorageId(id);
        return getUserByUsername(realm, storageId.getExternalId());
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        try {
            UserEntity userEntity = client.getUserByUsername(username);
            if (userEntity != null) {
                return fromUserEntity(userEntity);
            }

            return null;
        } catch (ApiException e) {
            if (e.getResponse().getStatus() == 404) {
                return null;
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        return null;
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        try {
            return client.listUsers()
                    .stream()
                    .map(this::fromUserEntity);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
        try {
            return client.listUsers()
                    .stream()
                    .map(this::fromUserEntity);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private UserModel fromUserEntity(UserEntity userEntity) {
        return new RestUserModel(session, realm, storageProviderModel, userEntity);
    }

}
