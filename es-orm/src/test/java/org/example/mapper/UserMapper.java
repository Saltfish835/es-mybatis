package org.example.mapper;

import org.example.bean.User;

import java.util.List;

public interface UserMapper {
    User queryUser();

    User queryUserByName(String name);

    List<User> queryUsers();

    List<User> query(User user);
}
