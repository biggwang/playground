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
    basePackages = "com.biggwang.racecondition.repository.lock",
    entityManagerFactoryRef = "lockEntityManager",
    transactionManagerRef = "lockTransactionManager"
)
public class DbLockConfiguration {

    private final DbProperties properties;

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean lockEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(lockDataSource());
        em.setPackagesToScan(new String[] { "com.biggwang.racecondition.repository.lock" });

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public HikariDataSource lockDataSource() {
        HikariConfig dataSource = new HikariConfig();
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        return new HikariDataSource(dataSource);
    }

    @Primary
    @Bean
    public PlatformTransactionManager lockTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(lockEntityManager().getObject());
        return transactionManager;
    }
}