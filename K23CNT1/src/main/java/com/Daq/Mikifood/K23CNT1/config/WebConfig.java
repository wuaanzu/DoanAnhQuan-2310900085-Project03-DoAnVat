package com.Daq.Mikifood.K23CNT1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Phục vụ hình ảnh đã upload từ thư mục static/uploads
        // Cache trong 3600 giây (1 giờ) để tăng hiệu suất
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/")
                .setCachePeriod(3600);

        // Phục vụ các thư viện WebJars (Bootstrap, jQuery, Font Awesome)
        // Không sử dụng resource chain để tránh xung đột version
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);

        // Phục vụ các file CSS tùy chỉnh
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // Phục vụ các file JavaScript tùy chỉnh
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // Phục vụ các hình ảnh tĩnh (logo, icons, v.v.)
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
