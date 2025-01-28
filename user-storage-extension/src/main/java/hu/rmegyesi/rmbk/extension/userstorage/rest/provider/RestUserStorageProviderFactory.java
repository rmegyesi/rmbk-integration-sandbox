package hu.rmegyesi.rmbk.extension.userstorage.rest.provider;

import com.google.auto.service.AutoService;
import hu.rmegyesi.rmbk.extension.userstorage.config.ClientConfig;
import hu.rmegyesi.rmbk.extension.userstorage.rest.api.UserResourceApi;
import hu.rmegyesi.rmbk.extension.userstorage.rest.client.UserStorageClient;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

@AutoService(UserStorageProviderFactory.class)
public class RestUserStorageProviderFactory implements UserStorageProviderFactory<RestUserStorageProvider> {

    public static final String PROVIDER_ID = "rest-user-storage";

    @Override
    public RestUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        UserResourceApi client = createClient(model);
        return new RestUserStorageProvider(session, session.getContext().getRealm(), model, client);
    }

    public UserResourceApi createClient(ComponentModel model) {
        ClientConfig clientConfig = new ClientConfig(model.getConfig());
        return UserStorageClient.createClient(clientConfig, UserResourceApi.class);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return ClientConfig.CONFIG_PROPERTIES;
    }
}
