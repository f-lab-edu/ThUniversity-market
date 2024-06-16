package university.market.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import university.market.config.interceptor.ThreadLocalCleanupInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ThreadLocalCleanupInterceptor threadLocalCleanupInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(threadLocalCleanupInterceptor);
    }
}
