package com.example.demo.configuration;

import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final List<String> allowCORSProfiles = Arrays.asList("dev", "dev2", "debug");
    private final CustomHttpMessageConverter converter;
    private final Environment env;

    @Autowired
    public WebConfig(CustomHttpMessageConverter converter, Environment env) {
        this.converter = converter;
        this.env = env;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);

        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringHttpMessageConverter);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(converter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        val profiles = Arrays.asList(env.getActiveProfiles());
        boolean allowCORS = profiles.isEmpty();
        for (val profile : profiles) {
            if (allowCORSProfiles.contains(profile)) {
                allowCORS = true;
                break;
            }
        }
        if (allowCORS) {
            registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*").maxAge(600);
        }
    }

}
