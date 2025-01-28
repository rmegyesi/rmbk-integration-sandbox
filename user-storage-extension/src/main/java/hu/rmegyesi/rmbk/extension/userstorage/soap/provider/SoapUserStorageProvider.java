package hu.rmegyesi.rmbk.extension.userstorage.soap.provider;

import hu.rmegyesi.rmbk.extension.userstorage.soap.model.SoapUserModel;
import hu.rmegyesi.rmbk.userstorage.soap.UserEntity;
import hu.rmegyesi.rmbk.userstorage.soap.UserStorageSoapResource;
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

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class SoapUserStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider, CredentialInputValidator {

    private final KeycloakSession session;
    private final RealmModel realm;
    private final ComponentModel storageProviderModel;
    private final UserStorageSoapResource client;


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

        return client.authenticateUser(
                user.getUsername(),
                userCredentialModel.getValue()
        );
    }

    @Override
    public void close() {
        try {
            ((Closeable) client).close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        return client.getUsers()
                .stream()
                .map(this::fromUserEntity);
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
        return Stream.empty();
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
        return client.getUsers()
                .stream()
                .map(this::fromUserEntity);
    }

    private UserModel fromUserEntity(UserEntity userEntity) {
        return new SoapUserModel(session, realm, storageProviderModel, userEntity);
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        StorageId storageId = new StorageId(id);
        return getUserByUsername(realm, storageId.getExternalId());
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        UserEntity userEntity = client.getUserByUsername(username);
        if (userEntity != null) {
            return fromUserEntity(userEntity);
        }

        return null;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        return null;
    }
}
