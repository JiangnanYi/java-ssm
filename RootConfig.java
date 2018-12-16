package ssm;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ComponentScan(basePackages = "ssm")
@Import(DataSourceConfig.class)
public class RootConfig {
    @Bean
    public BeanNameAutoProxyCreator autoProxyCreator(){
        BeanNameAutoProxyCreator autoProxyCreator = new BeanNameAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        autoProxyCreator.setBeanNames("com.nf147.test01");
        autoProxyCreator.setInterceptorNames("transactionInterceptor");
        return autoProxyCreator;
    }
}
