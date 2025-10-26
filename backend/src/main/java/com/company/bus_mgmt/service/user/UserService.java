package com.company.bus_mgmt.service.user;

import com.company.bus_mgmt.web.dto.user.SetRolesRequest;
import com.company.bus_mgmt.web.dto.user.UserResponse;
import com.company.bus_mgmt.web.dto.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> list(String q, Pageable pageable);
    UserResponse get(Long id);
    UserResponse update(Long id, UserUpdateRequest req);
    void deactivate(Long id);
    UserResponse setRoles(Long id, SetRolesRequest req);
}
