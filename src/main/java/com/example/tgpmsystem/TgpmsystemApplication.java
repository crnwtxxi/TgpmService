package com.example.tgpmsystem;

import com.example.tgpmsystem.utils.ExcelUtils;
import com.example.tgpmsystem.utils.IdWorker;
import com.example.tgpmsystem.utils.RedisUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.Properties;

@Slf4j
@SpringBootApplication
@MapperScan("com.example.tgpmsystem.mapper")
public class TgpmsystemApplication {

    //不加这个声明，后面不能注入
    @Bean
    public IdWorker createIdWorker(){
        return new IdWorker(0,0);
    }

    public static void main(String[] args) {
        log.info("TgpmsystemApplication run...");
        SpringApplication.run(TgpmsystemApplication.class, args);
    }

    @Bean
    public RedisUtils createRedisUtils(){
        return new RedisUtils();
    }

    //配置文件上传
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        // resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(40960);
        // 上传文件大小 5G
        resolver.setMaxUploadSize(5 * 1024 * 1024 * 1024);
        return resolver;
    }

    @Bean
    public ExcelUtils createExcelUtils() {
        return new ExcelUtils();
    }

    //配置分页
    @Bean
    public PageHelper getPageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
