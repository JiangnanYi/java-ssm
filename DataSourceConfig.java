package ssm;


import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.io.IOException;
import java.util.Properties;

@Configuration
@MapperScan("com.ssm.mapper")
public class DataSourceConfig {

    private String driver = "com.mysql.jdbc.Driver";;

    private String url = "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&autoReconnect=true";

    private String username = "root";

    private String password = "root";

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    // mybatis的配置
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath*:mappers/*.xml"));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.ssm.mapper");// 别名，让*Mpper.xml实体类映射可以不加上具体包名
        return sqlSessionFactoryBean;
    }

    // 事务管理器 对mybatis操作数据库事务控制，spring使用jdbc的事务控制类
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name = "transactionInterceptor")
    public TransactionInterceptor interceptor() {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(dataSourceTransactionManager());
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED");
        transactionAttributes.setProperty("del*", "PROPAGATION_REQUIRED");
        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED");
        transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("find*", "PROPAGATION_REQUIRED,readOnly");
        transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");
        interceptor.setTransactionAttributes(transactionAttributes);
        return interceptor;
    }
}