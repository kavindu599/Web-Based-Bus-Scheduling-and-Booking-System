package com.company.bus_mgmt.web;

import com.company.bus_mgmt.BusScheduleManagementSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BusScheduleManagementSystemApplication.class)
class AuthIntegrationTest {

    @Test
    void contextLoads() {
        // Smoke: app context starts, Flyway runs
    }
}
