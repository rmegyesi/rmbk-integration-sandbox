package hu.rmegyesi.rmbk.extension.userstorage.soap.client;

import hu.rmegyesi.rmbk.extension.userstorage.config.ClientConfig;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class UserStorageClient {

    public static <T> T createClient(ClientConfig clientConfig, Class<T> portType) {
        TracingSupport tracingSupport = CDI.current().select(TracingSupport.class).get();

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

        factory.setServiceClass(portType);
        factory.setAddress(clientConfig.getBaseUri());
        factory.getFeatures().add(tracingSupport.getClientFeature());

        return factory.create(portType);
    }
}
