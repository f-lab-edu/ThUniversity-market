package university.market.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import university.market.config.handler.WebSocketHandler;
import university.market.config.interceptor.WebSocketInterceptor;
import university.market.config.interceptor.WebSocketLoggingInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Autowired
    private WebSocketLoggingInterceptor webSocketLoggingInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/message")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketInterceptor, webSocketLoggingInterceptor);
    }
}
