package com.levelup.levelup_academy.Config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class CssMimeTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        
        // Set correct MIME type for CSS files
        if (requestURI.endsWith(".css")) {
            httpResponse.setContentType("text/css; charset=utf-8");
        } else if (requestURI.endsWith(".js")) {
            httpResponse.setContentType("application/javascript; charset=utf-8");
        }

        chain.doFilter(request, response);
    }
}




