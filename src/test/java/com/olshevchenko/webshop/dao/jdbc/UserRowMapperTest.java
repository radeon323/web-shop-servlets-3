package com.olshevchenko.webshop.dao.jdbc;

import com.olshevchenko.webshop.entity.Gender;
import com.olshevchenko.webshop.service.security.entity.Role;
import com.olshevchenko.webshop.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oleksandr Shevchenko
 */
class UserRowMapperTest {
    private final User expectedUser = User.builder()
                                            .id(1)
                                            .email("gofgeeman33@gmail.com")
                                            .password("blackmesa33")
                                            .gender(Gender.MALE)
                                            .firstName("Gordon")
                                            .lastName("Freeman")
                                            .about("Rise and shine, Mr. Freeman.")
                                            .age(27)
                                            .role(Role.USER)
                                            .build();

    @Test
    void testMapRow() throws SQLException {
        UserRowMapper userRowMapper = new UserRowMapper();

        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt("id")).thenReturn(expectedUser.getId());
        when(resultSetMock.getString("email")).thenReturn(expectedUser.getEmail());
        when(resultSetMock.getString("password")).thenReturn(expectedUser.getPassword());
        when(resultSetMock.getString("gender")).thenReturn(String.valueOf(expectedUser.getGender()));
        when(resultSetMock.getString("firstname")).thenReturn(expectedUser.getFirstName());
        when(resultSetMock.getString("lastname")).thenReturn(expectedUser.getLastName());
        when(resultSetMock.getString("about")).thenReturn(expectedUser.getAbout());
        when(resultSetMock.getInt("age")).thenReturn(expectedUser.getAge());
        when(resultSetMock.getString("role")).thenReturn(String.valueOf(expectedUser.getRole()));

        User actualUser = userRowMapper.mapRow(resultSetMock,0);

        assertEquals(expectedUser, actualUser);
    }
}