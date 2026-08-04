package com.market.finder.interceptor;

import com.market.finder.security.SecurityContextFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleAccessInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private SecurityContextFacade securityContextFacade;

    @InjectMocks
    private RoleAccessInterceptor interceptor;

    @Test
    void testPreHandle_Unauthenticated_Allowed() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(false);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testPreHandle_AdminAllowedAnywhere() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(true);
        when(securityContextFacade.getCurrentUsername()).thenReturn("admin");
        when(securityContextFacade.getCurrentAuthorities()).thenReturn(Set.of("ROLE_ADMIN"));
        when(request.getRequestURI()).thenReturn("/admin/users");
        when(request.getMethod()).thenReturn("GET");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testPreHandle_InstructorDeniedAdminPath() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(true);
        when(securityContextFacade.getCurrentUsername()).thenReturn("instructor1");
        when(securityContextFacade.getCurrentAuthorities()).thenReturn(Set.of("ROLE_INSTRUCTOR"));
        when(request.getRequestURI()).thenReturn("/admin/users");
        when(request.getMethod()).thenReturn("GET");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response, times(1)).sendRedirect("/access-denied");
    }

    @Test
    void testPreHandle_StudentAllowedWithReadPermission() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(true);
        when(securityContextFacade.getCurrentUsername()).thenReturn("student1");
        when(securityContextFacade.getCurrentAuthorities()).thenReturn(Set.of("ROLE_STUDENT", "READ"));
        when(request.getRequestURI()).thenReturn("/students");
        when(request.getMethod()).thenReturn("GET");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void testPreHandle_StudentDeniedDeleteStudent() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(true);
        when(securityContextFacade.getCurrentUsername()).thenReturn("student1");
        when(securityContextFacade.getCurrentAuthorities()).thenReturn(Set.of("ROLE_STUDENT", "READ"));
        when(request.getRequestURI()).thenReturn("/students/delete/1");
        when(request.getMethod()).thenReturn("GET");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertFalse(result);
        verify(response, times(1)).sendRedirect("/access-denied");
    }
}
