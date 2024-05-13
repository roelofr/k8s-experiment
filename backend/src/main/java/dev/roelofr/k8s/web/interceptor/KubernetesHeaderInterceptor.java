package dev.roelofr.k8s.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class KubernetesHeaderInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        response.setHeader("X-Hostname", System.getenv("HOSTNAME"));

        log.info(
            String.format("[afterCompletion][%s] Request {} completed at {}", System.getenv("HOSTNAME")),
            request.getRequestURI(),
            System.currentTimeMillis()
        );
    }
}
