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

    @Mock
    private RouteAccessEvaluator routeAccessEvaluator;

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
    void testPreHandle_DelegatesToEvaluator() throws Exception {
        when(securityContextFacade.isAuthenticated()).thenReturn(true);
        when(securityContextFacade.getCurrentUsername()).thenReturn("user1");
        when(securityContextFacade.getCurrentAuthorities()).thenReturn(Set.of("READ"));
        when(request.getRequestURI()).thenReturn("/students");
        when(request.getMethod()).thenReturn("GET");
        when(routeAccessEvaluator.evaluate(eq(response), eq("user1"), eq("GET"), eq("/students"), eq(Set.of("READ")))).thenReturn(true);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
        verify(routeAccessEvaluator, times(1)).evaluate(eq(response), eq("user1"), eq("GET"), eq("/students"), eq(Set.of("READ")));
    }
}

