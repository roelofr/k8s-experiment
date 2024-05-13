package dev.roelofr.k8s.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Configuration
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        var context = event.getApplicationContext();
        var requestMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        var map = requestMapping.getHandlerMethods();
        log.info("Registered endpoints:");
        map.keySet().forEach((key) -> log.info("- {}", key));
    }
}
