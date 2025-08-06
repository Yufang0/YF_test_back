package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * CORS 配置類別
 * 處理跨域請求設定，允許前端應用程式存取後端 API
 * 
 * @author System Generated
 * @version 1.0
 * @since 2025-08-05
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 全域 CORS 配置
     * 適用於所有控制器
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4200", // Angular 預設開發端口
                        "http://localhost:4201", // 您的自訂開發端口
                        "http://127.0.0.1:4200",
                        "http://127.0.0.1:4201")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 預檢請求快取時間（秒）
    }

    /**
     * CORS 過濾器 Bean
     * 提供更細緻的 CORS 控制
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允許的來源
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://127.0.0.1:*"));

        // 允許的 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 允許的標頭
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 允許攜帶認證資訊
        configuration.setAllowCredentials(true);

        // 預檢請求快取時間
        configuration.setMaxAge(3600L);

        // 暴露的標頭（前端可以存取的回應標頭）
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "X-Total-Count"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return new CorsFilter(source);
    }
}
