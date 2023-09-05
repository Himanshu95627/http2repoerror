package com.WebSocketWithHttp.WebsocketWithHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    public static final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        sessions.add(session);

        TextMessage message = new TextMessage("one-time message from server");
        logger.info("Server sends: {}", message);
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 10000)
    void sendPeriodicMessages() throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                String broadcast = "server periodic message " + LocalTime.now()+ Thread.currentThread().getName();
                logger.info("Server sends: {}", broadcast);
                session.sendMessage(new TextMessage(broadcast));
            }
        }
    }

//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String request = message.getPayload();
//        logger.info("Server received: {}", request);
//
//        String response = String.format("response from server to '%s'", HtmlUtils.htmlEscape(request));
//        logger.info("Server sends: {}", response);
//        session.sendMessage(new TextMessage(response));
//    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
        return;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        System.out.println(message.getPayload().getClass());
        String receivedPayload = (String) message.getPayload();

        // Deserialize the incoming message into an instance of InputClass
        InputClass input = objectMapper.readValue(receivedPayload, InputClass.class);

        // Custom processing logic based on the received InputClass
        String responseMessage = "Received message: " + input.getMessage() + ", Value: " + input.getValue();

        // Send the response back to the client
//        Thread.sleep(1000);

        for(WebSocketSession ssn:sessions){
            ssn.sendMessage(message);
        }

//        session.sendMessage(message);

    }

    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }
}