package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private DosProtectionService dosProtectionService;
    private String ip = "192.168.0.3";
    private int TOO_MANY_REQUESTS_CODE = 429;

    @InjectMocks
    private DosFilter dosFilter = new DosFilter();

    @Before
    public void setup() throws ServletException {
        when(request.getRemoteAddr()).thenReturn(ip);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        when(dosProtectionService.isAllowed(ip)).thenReturn(true);
        dosFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterNotAllowed() throws IOException, ServletException {
        when(dosProtectionService.isAllowed(ip)).thenReturn(false);
        dosFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(TOO_MANY_REQUESTS_CODE);
    }
}
