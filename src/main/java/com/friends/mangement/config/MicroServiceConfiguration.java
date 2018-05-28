package com.friends.mangement.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.friends.mangement.constants.Constants;

@Configuration
@EnableJpaRepositories(Constants.DAOPACKAGE)
public class MicroServiceConfiguration {

    @Value("${dbUsername}")
    String dbUsername;

    @Value("${dbPassword}")
    String dbPassword;

    @Value("${dbServerUrl}")
    String dbServerUrl;


    @Value("${dbDriverClass}")
    String dbDriverClass;

    @Value("${dbServerDialect}")
    String dbServerDialect;

    @Bean(name = "datasource")
    public DriverManagerDataSource getDriverManagerDataSource() {
        DriverManagerDataSource driverManagerDataSource =
                new DriverManagerDataSource();
        driverManagerDataSource.setUsername(dbUsername);
        driverManagerDataSource.setPassword(dbPassword);
        driverManagerDataSource.setUrl(dbServerUrl);
        driverManagerDataSource.setDriverClassName(dbDriverClass);
        return driverManagerDataSource;
    }


    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getLocalContainerEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter jpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabasePlatform(dbServerDialect);
        localContainerEntityManagerFactoryBean
                .setJpaVendorAdapter(jpaVendorAdapter);
        localContainerEntityManagerFactoryBean
                .setDataSource(getDriverManagerDataSource());
        localContainerEntityManagerFactoryBean
                .setPackagesToScan(Constants.MODELPACKAGE);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager getJpaTransactionManager(
            EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
