package hu.rmegyesi.rmbk.userstorage.soap;

import hu.rmegyesi.rmbk.userstorage.data.UserEntity;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.List;

@WebService(name = "UserStorageSoapResource", serviceName = "UserStorageSoapResource")
public interface UserStorageSoapResource {

    @WebMethod
    List<UserEntity> getUsers();

    @WebMethod
    UserEntity getUserByUsername(@WebParam(name = "username") String username);

    @WebMethod
    boolean authenticateUser(@WebParam(name = "username") String username, @WebParam(name = "password") String password);
}
