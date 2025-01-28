package hu.rmegyesi.rmbk.userstorage.service;

import hu.rmegyesi.rmbk.userstorage.data.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class UserStorageService {

    public List<UserEntity> listUsers() {
        return UserEntity.listAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return UserEntity.findByIdOptional(id);
    }

    public Optional<UserEntity> getUserByUsername(String username) {
        return UserEntity
                .find("username", username)
                .firstResultOptional();
    }

    public boolean authenticate(String username, String password) {
        return getUserByUsername(username)
                .map(userEntity -> Objects.equals(userEntity.password, password))
                .orElse(false);
    }
}
