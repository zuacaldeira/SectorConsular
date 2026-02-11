package ao.gov.sgcd.pm.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_withValidBearerToken_shouldSetSecurityContext() throws Exception {
        String token = "valid-jwt-token";
        request.addHeader("Authorization", "Bearer " + token);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(token)).thenReturn("admin");
        when(tokenProvider.getRoleFromToken(token)).thenReturn("DEVELOPER");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("admin", authentication.getName());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DEVELOPER")));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withStakeholderToken_shouldSetStakeholderRole() throws Exception {
        String token = "stakeholder-jwt-token";
        request.addHeader("Authorization", "Bearer " + token);

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getUsernameFromToken(token)).thenReturn("stakeholder");
        when(tokenProvider.getRoleFromToken(token)).thenReturn("STAKEHOLDER");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("stakeholder", authentication.getName());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STAKEHOLDER")));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withMissingAuthorizationHeader_shouldNotSetSecurityContext() throws Exception {
        // No Authorization header set

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenProvider);
    }

    @Test
    void doFilterInternal_withInvalidToken_shouldNotSetSecurityContext() throws Exception {
        String token = "invalid-token";
        request.addHeader("Authorization", "Bearer " + token);

        when(tokenProvider.validateToken(token)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(tokenProvider, never()).getUsernameFromToken(anyString());
        verify(tokenProvider, never()).getRoleFromToken(anyString());
    }

    @Test
    void doFilterInternal_withNonBearerAuthorizationHeader_shouldNotSetSecurityContext() throws Exception {
        request.addHeader("Authorization", "Basic dXNlcjpwYXNz");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(tokenProvider);
    }

    @Test
    void doFilterInternal_withEmptyBearerToken_shouldNotSetSecurityContext() throws Exception {
        request.addHeader("Authorization", "Bearer ");

        when(tokenProvider.validateToken("")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldAlwaysCallFilterChain() throws Exception {
        // Even with no auth header, filter chain must be called
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_withOnlyBearerKeyword_shouldNotSetSecurityContext() throws Exception {
        request.addHeader("Authorization", "Bearer");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
