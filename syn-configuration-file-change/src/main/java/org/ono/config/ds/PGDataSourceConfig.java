package org.ono.config.ds;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ono on 2018/12/4.
 */
@Configuration
@MapperScan(basePackages = PGDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "pgSqlSessionFactory")
public class PGDataSourceConfig {
    static final String PACKAGE = "org.ono.config.ds";
    static final String MAPPER_LOCATION = "classpath:mapper/pg/*.xml";

//    db.datasource.url=jdbc:postgresql://localhost:5432/postgres
//    db.datasource.username=postgres
//    db.datasource.password=root
//    db.datasource.driverClassName=org.postgresql.Driver

    @Value("${db.datasource.url}")
    private String url;
    @Value("${db.datasource.username}")
    private String user;
    @Value("${db.datasource.password}")
    private String password;
    @Value("${db.datasource.driverClassName}")
    private String driverClass;

    @Bean(name="pgDataSource")
    @Primary
    public DataSource pgDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "pgTransactionManager")
    @Primary
    public DataSourceTransactionManager pgTransactionManager(){
        return new DataSourceTransactionManager(pgDataSource());
    }

    @Bean(name ="pgSqlSessionFactory")
    public SqlSessionFactory pgSqlSessionFactory(@Qualifier("pgDataSource") DataSource pgDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(pgDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(PGDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

}
