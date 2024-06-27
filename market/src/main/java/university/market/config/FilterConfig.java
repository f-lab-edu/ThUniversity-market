package university.market.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import university.market.common.CachingFilter;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CachingFilter> contentCachingFilter() {
        FilterRegistrationBean<CachingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingFilter());
        registrationBean.addUrlPatterns("/api/member/*", "/api/dibs/*", "/api/item/*", "/api/offer/*", "/api/email/*");
        registrationBean.setOrder(1);
        registrationBean.setName("CachingFilter");
        return registrationBean;
    }
}
