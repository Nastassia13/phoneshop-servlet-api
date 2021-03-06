package com.es.phoneshop.web.filter;

import com.es.phoneshop.security.DosProtectionService;
import com.es.phoneshop.security.impl.DefaultDosProtectionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;
    private int TOO_MANY_REQUESTS_CODE = 429;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            filterChain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).setStatus(TOO_MANY_REQUESTS_CODE);
        }
    }

    @Override
    public void destroy() {

    }
}
