package university.market.config;

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

    private final WebSocketHandler webSocketHandler;

    private final WebSocketInterceptor webSocketInterceptor;

    private final WebSocketLoggingInterceptor webSocketLoggingInterceptor;

    public WebSocketConfig(WebSocketHandler webSocketHandler, WebSocketInterceptor webSocketInterceptor,
                           WebSocketLoggingInterceptor webSocketLoggingInterceptor) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketInterceptor = webSocketInterceptor;
        this.webSocketLoggingInterceptor = webSocketLoggingInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws/message")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketInterceptor, webSocketLoggingInterceptor);
    }
}
