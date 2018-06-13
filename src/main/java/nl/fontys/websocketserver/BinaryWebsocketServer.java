package nl.fontys.websocketserver;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/images")
public class BinaryWebsocketServer {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId() + " connected!");
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    
    @OnError
    public void onError(Throwable t){
        Logger.getLogger(BinaryWebsocketServer.class.getName()).log(Level.SEVERE, null, t);
    }

    @OnMessage
    public void onMessage(ByteBuffer byteBuffer){
        for(Session session : sessions){
            try{
                session.getBasicRemote().sendBinary(byteBuffer);
            }catch (IOException e){
                Logger.getLogger(BinaryWebsocketServer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
