package university.market.config.intercepter;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
public class WebSocketLoggingInterceptor implements HandshakeInterceptor {
    private static final String WS_LOG_FORMAT = """
            WebSocket Handshake:
                Request URI: {}
                Authorization: {}
                Attributes: {}
            ====================
            WebSocket Response:
                Headers: {}
            """;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        log.info(
                WS_LOG_FORMAT,
                request.getURI(),
                request.getHeaders().get("Authorization"),
                attributes,
                response.getHeaders()
        );
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
