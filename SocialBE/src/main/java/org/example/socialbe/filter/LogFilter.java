package org.example.socialbe.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.socialbe.util.LogUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(-1000)
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        LogUtil.generateTraceId();
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        log.info("Request start {} {}", req.getMethod(), uri);
        res.setHeader("X-Trace-Id", LogUtil.getTraceId());
        filterChain.doFilter(req, res);
        log.info("Request End");
    }
}
