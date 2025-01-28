package hu.rmegyesi.rmbk.extension.userstorage.config;

import org.keycloak.common.util.MultivaluedHashMap;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.List;

import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

public class ClientConfig {

    interface Properties {
        String BASE_URI = "base-uri";
        String CONNECTION_TIMEOUT = "connection-timeout";
        String READ_TIMEOUT = "read-timeout";
    }

    public static final List<ProviderConfigProperty> CONFIG_PROPERTIES = ProviderConfigurationBuilder.create()
            .property()
            .name(Properties.BASE_URI)
            .label("Base URI")
            .type(STRING_TYPE)
            .defaultValue("http://localhost:8080")
            .required(true)
            .add()

            .build();

    private final MultivaluedHashMap<String, String> configMap;

    public ClientConfig(MultivaluedHashMap<String, String> configMap) {
        this.configMap = configMap;
    }

    public String getBaseUri() {
        return configMap.getFirst(Properties.BASE_URI);
    }


}
