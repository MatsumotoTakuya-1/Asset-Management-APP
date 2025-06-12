package com.example.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration : WebMvcConfigurer {

    //公式から。なくても動く
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/resources/**")
            .addResourceLocations("/public", "classpath:/static/")
    }

    // 「指定URL叩いたら index.html にリダイレクト」うまくいかない？
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/input/assets/2025-06").setViewName("index")
        registry.addViewController("/input/transactions/2025-06").setViewName("index")
    }
}