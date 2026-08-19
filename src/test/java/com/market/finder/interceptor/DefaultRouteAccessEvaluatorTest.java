package com.market.finder.interceptor;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultRouteAccessEvaluatorTest {

    @Mock
    private HttpServletResponse response;

    private DefaultRouteAccessEvaluator evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DefaultRouteAccessEvaluator();
    }

    @Test
    void testAdminEndpoint_RequiresManageUsers() throws Exception {
        boolean allowed = evaluator.evaluate(response, "user", "GET", "/admin/users", Set.of("READ"));
        assertFalse(allowed);
        verify(response, times(1)).sendRedirect("/access-denied");

        reset(response);

        boolean allowedWithManage = evaluator.evaluate(response, "user", "GET", "/admin/users", Set.of("MANAGE_USERS"));
        assertTrue(allowedWithManage);
    }

    @Test
    void testDeleteOperation_RequiresDeletePermission() throws Exception {
        boolean allowed = evaluator.evaluate(response, "user", "GET", "/students/delete/1", Set.of("READ"));
        assertFalse(allowed);

        reset(response);

        boolean allowedWithDelete = evaluator.evaluate(response, "user", "GET", "/students/delete/1", Set.of("DELETE"));
        assertTrue(allowedWithDelete);
    }

    @Test
    void testWriteOperation_RequiresWritePermission() throws Exception {
        boolean allowed = evaluator.evaluate(response, "user", "POST", "/students/save", Set.of("READ"));
        assertFalse(allowed);

        reset(response);

        boolean allowedWithWrite = evaluator.evaluate(response, "user", "POST", "/students/save", Set.of("WRITE"));
        assertTrue(allowedWithWrite);
    }

    @Test
    void testReadOperation_RequiresReadPermission() throws Exception {
        boolean allowed = evaluator.evaluate(response, "user", "GET", "/students", Set.of());
        assertFalse(allowed);

        reset(response);

        boolean allowedWithRead = evaluator.evaluate(response, "user", "GET", "/students", Set.of("READ"));
        assertTrue(allowedWithRead);
    }

    @Test
    void testRoleAdmin_AllowedAnywhere() throws Exception {
        boolean allowedAdmin = evaluator.evaluate(response, "adminUser", "DELETE", "/admin/users/delete/test", Set.of("ROLE_ADMIN"));
        assertTrue(allowedAdmin);
        verify(response, never()).sendRedirect(anyString());
    }
}
