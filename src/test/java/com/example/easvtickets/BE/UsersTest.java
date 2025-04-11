package com.example.easvtickets.BE;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        int loginId = 1;
        String username = "testUser";
        String passwordHash = "hashedPassword";
        boolean isAdmin = true;
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        // Act
        Users user = new Users(loginId, username, passwordHash, isAdmin, createdAt);

        // Assert
        assertEquals(loginId, user.getLoginid());
        assertEquals(username, user.getUsername());
        assertEquals(passwordHash, user.getPasswordhash());
        assertEquals(isAdmin, user.getIsadmin());
        assertEquals(createdAt, user.getCreatedat());
    }

    @Test
    public void testSetters() {
        // Arrange
        Users user = new Users(0, null, null, false, null);
        int loginId = 2;
        String username = "updatedUser";
        String passwordHash = "newHash";
        boolean isAdmin = true;
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        // Act
        user.setLoginid(loginId);
        user.setUsername(username);
        user.setPasswordhash(passwordHash);
        user.setIsadmin(isAdmin);
        user.setCreatedat(createdAt);

        // Assert
        assertEquals(loginId, user.getLoginid());
        assertEquals(username, user.getUsername());
        assertEquals(passwordHash, user.getPasswordhash());
        assertEquals(isAdmin, user.getIsadmin());
        assertEquals(createdAt, user.getCreatedat());
    }

}