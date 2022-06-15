package com.min.reggie.config;

import com.min.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 消息转换器
     * @param converters 消息转换器list集合
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);  // 添加到集合的第一个
    }

    /**
     * 设置文件资源映射
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射");
        // 后端路径： 当访问backend开头的路径，自动访问到/backend/路径下
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        // 前段路径： 当访问front开头的路径，自动访问到/front/路径下
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
