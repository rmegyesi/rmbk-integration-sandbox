package hu.rmegyesi.rmbk.extension.userstorage.soap.provider;

import com.google.auto.service.AutoService;
import hu.rmegyesi.rmbk.extension.userstorage.config.ClientConfig;
import hu.rmegyesi.rmbk.extension.userstorage.soap.client.UserStorageClient;
import hu.rmegyesi.rmbk.userstorage.soap.UserStorageSoapResource;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.List;

@AutoService(UserStorageProviderFactory.class)
public class SoapUserStorageProviderFactory implements UserStorageProviderFactory<SoapUserStorageProvider> {

    public static final String PROVIDER_ID = "soap-user-storage";

    @Override
    public SoapUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        UserStorageSoapResource client = createClient(model);
        return new SoapUserStorageProvider(session, session.getContext().getRealm(), model, client);
    }

    public UserStorageSoapResource createClient(ComponentModel model) {
        ClientConfig clientConfig = new ClientConfig(model.getConfig());
        return UserStorageClient.createClient(clientConfig, UserStorageSoapResource.class);
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
