package W1D4.websockets.sockets;

import java.util.ArrayList;
import java.util.List;

import W1D4.websockets.domain.Message;
import W1D4.websockets.service.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Log the received message
        System.out.println("Received message: " + message.getPayload());

        // Parse the incoming JSON message
        JsonNode jsonNode = objectMapper.readTree(message.getPayload());
        String sender = jsonNode.get("sender").asText();
        String content = jsonNode.get("content").asText();

        // Save the message to the database
        messageRepository.save(new Message(sender, content));

        // Broadcast the message to all connected clients
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("New connection established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }
}