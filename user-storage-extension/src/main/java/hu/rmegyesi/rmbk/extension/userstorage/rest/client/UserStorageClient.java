package hu.rmegyesi.rmbk.extension.userstorage.rest.client;

import hu.rmegyesi.rmbk.extension.userstorage.config.ClientConfig;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.annotation.Nonnull;

import java.net.URI;

public class UserStorageClient {

    public static <T> T createClient(@Nonnull ClientConfig config, @Nonnull Class<T> type) {
        return QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(config.getBaseUri()))
                .build(type);
    }
}
