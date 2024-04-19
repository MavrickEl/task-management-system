package com.example.userservice.provider;

import com.example.userservice.enums.Role;
import com.example.userservice.model.User;

public class UserProvider {
    public static User getUser() {
        return User.builder().id(1L).name("Jim").secondName("Jones").email("Jim.Jones@example.com").password("password").role(Role.USER).build();
    }

    public static User getUserForSave() {
        return User.builder().id(null).name("Jim").secondName("Jones").email("Jim.Jones@example.com").password("password").role(Role.USER).build();
    }

    public static User getUpdateUserWithId() {
        return User.builder().id(1L).name("Jon").secondName("Jes").email("Jon.Jes@example.com").password("updatePassword").role(Role.ADMIN).build();
    }

    public static User getUpdateUserWithoutId() {
        return User.builder().id(null).name("Jon").secondName("Jes").email("Jon.Jes@example.com").password("updatePassword").role(Role.ADMIN).build();
    }
}
