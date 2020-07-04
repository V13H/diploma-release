package com.hvs.diploma.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.hvs.diploma.dao.sms",
        entityManagerFactoryRef = "smsEntityManager",
        transactionManagerRef = "smsTransactionManager"
)
public class PersistenceTurboSmsConfig {
    @Autowired
    private Environment environment;

    @Bean
    @ConfigurationProperties(prefix = "spring.sms-datasource")
    public DataSource smsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean smsEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(smsDataSource());
        em.setPackagesToScan("com.hvs.diploma.turbo_sms");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
//        HashMap<String, Object> properties = new HashMap<>();
//        properties.put("hibernate.dialect", environment.getProperty("hibernate.dialect"));
//        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public PlatformTransactionManager smsTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(smsEntityManager().getObject());
        return transactionManager;
    }
}
