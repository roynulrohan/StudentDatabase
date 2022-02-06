package com.roynulrohan.studentdatabaseapi.authorization;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccountRequestFilter extends GenericFilterBean {
    RequestMatcher customFilterUrl = new AntPathRequestMatcher("/api/account/{accountId}/students/**");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (customFilterUrl.matches(httpServletRequest)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null) {
                Long accountId = Long.valueOf(httpServletRequest.getRequestURI().split("/")[3]);

                if (!accountId.equals(Long.valueOf(authentication.getPrincipal().toString()))) {
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
                }
            }

        }

        chain.doFilter(request, response);
    }
}
