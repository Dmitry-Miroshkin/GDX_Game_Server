package GDX_Game_server.server.ws;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;


@Component
public class WebSocketHandler extends AbstractWebSocketHandler {
    private final Array<StandardWebSocketSession> sessions = new Array<>();

    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;
    private MessageListener messageListener;
    private final JsonReader reader = new JsonReader();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        synchronized (sessions){

            sessions.add(standardWebSocketSession);
            connectListener.handle(standardWebSocketSession);
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        String payload = message.getPayload();
        JsonValue jsonValue = reader.parse(payload);

        messageListener.handle(standardWebSocketSession, jsonValue);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        StandardWebSocketSession standardWebSocketSession = (StandardWebSocketSession) session;
        synchronized (sessions){
            sessions.removeValue(standardWebSocketSession, true);
            disconnectListener.handle(standardWebSocketSession);
        }

    }

    public void setConnectListener(ConnectListener connectListener) {

        this.connectListener = connectListener;
    }

    public void setDisconnectListener(DisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public Array<StandardWebSocketSession> getSessions() {
        return sessions;
    }
}
