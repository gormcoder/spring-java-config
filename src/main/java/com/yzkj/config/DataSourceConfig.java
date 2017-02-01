package com.yzkj.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;



/**
 * 配置文件
 * Created by Gorm on 2017/2/1.
 */

@Configuration
@PropertySource({"classpath:/properties/jdbc.properties"})

public class DataSourceConfig {

    private static final Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Value("${jdbc.driver}")
    String driverClass;
    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.username}")
    String userName;
    @Value("${jdbc.password}")
    String passWord;


    @Bean(name="dataSource")
    public DataSource dataSource(){

        logger.info("DataSource");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        return dataSource;
    }

}
