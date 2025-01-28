package hu.rmegyesi.rmbk.userstorage.rest;

import hu.rmegyesi.rmbk.userstorage.data.UserEntity;
import hu.rmegyesi.rmbk.userstorage.service.UserStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;

@Path("/users")
@ApplicationScoped
public class UserResource {

    @Inject
    UserStorageService userStorageService;

    @GET
    public List<UserEntity> listUsers() {
        return userStorageService.listUsers();
    }

    @GET
    @Path("{username}")
    public RestResponse<UserEntity> getUserByUsername(@PathParam("username") String username) {
        return userStorageService.getUserByUsername(username)
                .map(RestResponse::ok)
                .orElse(RestResponse.notFound());
    }

    public record AuthenticateUserBody(String username, String password) {}

    @POST
    @APIResponses({
            @APIResponse(description = "Authentication successful", responseCode = "200"),
            @APIResponse(description = "Authentication failed", responseCode = "401")
    })
    public Response authenticate(AuthenticateUserBody body) {
        boolean result = userStorageService.authenticate(body.username(), body.password());
        if (result) {
            return Response.ok().build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
