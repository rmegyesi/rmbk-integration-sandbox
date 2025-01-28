package hu.rmegyesi.rmbk.extension.userstorage.soap.model;

import hu.rmegyesi.rmbk.userstorage.soap.UserEntity;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.ReadOnlyException;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class SoapUserModel extends AbstractUserAdapterFederatedStorage {

    private final UserEntity user;

    public SoapUserModel(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, UserEntity user) {
        super(session, realm, storageProviderModel);
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public void setUsername(String username) {
        throw new ReadOnlyException("Username cannot be changed");
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public boolean isEmailVerified() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Long getCreatedTimestamp() {
        return user.getCreatedAt().toGregorianCalendar().getTimeInMillis();
    }

    @Override
    public String getFirstAttribute(String name) {
        return switch (name) {
            case ENABLED, EMAIL_VERIFIED -> "true";
            case FIRST_NAME -> user.getFirstName();
            case LAST_NAME -> user.getLastName();
            case EMAIL -> user.getEmail();
            default -> super.getFirstAttribute(name);
        };
    }

    @Override
    public Stream<String> getAttributeStream(String name) {
        List<String> strings = getAttributes().get(name);
        if (strings == null) {
            return Stream.empty();
        } else {
            return strings.stream();
        }
    }

    @Override
    public Map<String, List<String>> getAttributes() {
        var attributes = Map.of(
                ENABLED, List.of("true"),
                EMAIL_VERIFIED, List.of("true"),
                FIRST_NAME, List.of(user.getFirstName()),
                LAST_NAME, List.of(user.getLastName()),
                EMAIL, List.of(user.getEmail())
        );

        Map<String, List<String>> storedAttributes = super.getAttributes();
        storedAttributes.putAll(attributes);

        return storedAttributes;
    }
}
