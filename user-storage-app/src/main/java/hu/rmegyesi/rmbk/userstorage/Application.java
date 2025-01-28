package hu.rmegyesi.rmbk.userstorage;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "User Storage API",
                version = "1.0-SNAPSHOT"
        )
)
public class Application extends jakarta.ws.rs.core.Application {
}
