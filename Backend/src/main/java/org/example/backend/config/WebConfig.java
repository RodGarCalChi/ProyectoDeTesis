package org.example.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private RoleValidationInterceptor roleValidationInterceptor;
    
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(roleValidationInterceptor)
                .addPathPatterns("/api/movimientos/**")
                .excludePathPatterns("/api/auth/**"); // Excluir rutas de autenticación
    }
    
    // CORS configuration moved to SecurityConfig to avoid conflicts
}