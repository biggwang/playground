package com.biggwang.racecondition.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories(
    basePackages = "com.biggwang.racecondition.repository.service",
    entityManagerFactoryRef = "serviceEntityManager",
    transactionManagerRef = "serviceTransactionManager"
)
public class DbServiceConfiguration {

    private final DbProperties properties;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean serviceEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(serviceDataSource());
        em.setPackagesToScan(new String[] { "com.biggwang.racecondition.repository.service" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public HikariDataSource serviceDataSource() {
        HikariConfig dataSource = new HikariConfig();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return new HikariDataSource(dataSource);
    }

    @Primary
    @Bean
    public PlatformTransactionManager serviceTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(serviceEntityManager().getObject());
        return transactionManager;
    }
}