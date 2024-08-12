package university.market.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "university.market")
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.config-location}")
    private String configLocation;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);

        // 설정된 `mapper-locations`를 반영하여 XML 매퍼 파일 위치 설정
        Resource[] mapperLocations = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml");
        sessionFactoryBean.setMapperLocations(mapperLocations);

        // 설정된 `config-location`을 반영하여 MyBatis 설정 파일 위치 설정
        sessionFactoryBean.setConfigLocation(
                new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml")
        );

        return sessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
