package com.company.bus_mgmt.service;

import com.company.bus_mgmt.domain.user.User;
import com.company.bus_mgmt.repository.user.UserRepository;
import com.company.bus_mgmt.service_impl.user.UserServiceImpl;
import com.company.bus_mgmt.web.dto.user.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    @Test
    void updateUserName() {
        var repo = Mockito.mock(UserRepository.class);
        var svc = new UserServiceImpl(repo);
        var u = new User();
        u.setEmail("x@y.z"); u.setFullName("Old");
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(repo.save(Mockito.any())).thenAnswer(a -> a.getArgument(0));

        var resp = svc.update(1L, new UserUpdateRequest("New Name", null));
        assertEquals("New Name", resp.fullName());
    }
}
