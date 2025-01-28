package hu.rmegyesi.rmbk.userstorage.soap;

import hu.rmegyesi.rmbk.userstorage.data.UserEntity;
import hu.rmegyesi.rmbk.userstorage.service.UserStorageService;
import jakarta.inject.Inject;
import jakarta.jws.WebService;

import java.util.List;

@WebService(serviceName = "UserStorageSoapResource")
public class UserStorageSoapResourceImpl implements UserStorageSoapResource {

    @Inject
    UserStorageService userStorageService;

    @Override
    public List<UserEntity> getUsers() {
        return userStorageService.listUsers();
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userStorageService.getUserByUsername(username)
                .orElse(null);
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        return userStorageService.authenticate(username, password);
    }
}
