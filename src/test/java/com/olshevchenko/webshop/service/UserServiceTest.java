package com.olshevchenko.webshop.service;

import com.olshevchenko.webshop.dao.UserDao;
import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.entity.Role;
import com.olshevchenko.webshop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Oleksandr Shevchenko
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDaoMock;

    private UserService userService;

    private User expectedUser;

    @BeforeEach
    void init() {
        userService = new UserService(userDaoMock);

        expectedUser = User.builder()
                .id(1)
                .email("darthvader@gmail.com")
                .password(" �vX���i�G��\u0006\u0005JKJ:�\u0012Y��k]�GB��1�Y�G\u001A�\u0001\u0011*���Y��t��\u0011��\u0006�Y�����s���")
                .gender(Gender.MALE)
                .firstName("Darth")
                .lastName("Vader")
                .about("May the force be with you!")
                .age(50)
                .role(Role.USER)
                .build();
    }

    @Test
    void testFindById() {
        when(userDaoMock.findById(1)).thenReturn(Optional.ofNullable(expectedUser));
        Optional<User> optionalUser = userService.findById(1);
        User actualUser = optionalUser.get();
        assertEquals(expectedUser, actualUser);
        verify(userDaoMock, times(1)).findById(1);
    }

    @Test
    void testFindByEmail() {
        when(userDaoMock.findByEmail("darthvader@gmail.com")).thenReturn(Optional.ofNullable(expectedUser));
        Optional<User> optionalUser = userService.findByEmail("darthvader@gmail.com");
        User actualUser = optionalUser.get();
        assertEquals(expectedUser, actualUser);
        verify(userDaoMock, times(1)).findByEmail("darthvader@gmail.com");
    }

    @Test
    void testAdd() {
        doNothing().when(userDaoMock).add(isA(User.class));
        userDaoMock.add(expectedUser);
        verify(userDaoMock, times(1)).add(expectedUser);
    }

    @Test
    void testRemove() {
        doNothing().when(userDaoMock).remove(isA(Integer.class));
        userDaoMock.remove(1);
        verify(userDaoMock, times(1)).remove(1);
    }

    @Test
    void testEdit() {
        doNothing().when(userDaoMock).update(isA(User.class));
        userDaoMock.update(expectedUser);
        verify(userDaoMock, times(1)).update(expectedUser);
    }


}