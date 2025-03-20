package com.example.easvtickets.DAL;

import com.example.easvtickets.BE.Users;

import java.util.List;

public interface IUserDataAccess {

    List<Users> getAllUsers() throws Exception;

    Users createUser(Users newUser) throws Exception;

    void deleteUser(Users user) throws Exception;

    void updateUser(Users user) throws Exception;

    Users getUsername(String username);
}
