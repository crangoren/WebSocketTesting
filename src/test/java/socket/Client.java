package socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class Client extends WebSocketClient {

    private Date openedTime;

    private final SocketContext socketContext;

    public Client(SocketContext socketContext) throws URISyntaxException {
        super(new URI(socketContext.getURI()));
        this.socketContext = socketContext;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        openedTime = new Date();
        System.out.println("Opened new connection " + socketContext.getURI());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message " + message);
        socketContext.getMessageList().add(message);
        if(message.equals(socketContext.getExpectedMessage())) {
            closeConnection(1000, "Expected message received");
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Close socket with code " + code + ", reason: " + reason);
        socketContext.setStatusCode(code);
    }

    @Override
    public void onError(Exception ex) {

    }

    public int getAliveTime(){
        Date closeDate = new Date();
        int timeInSeconds = (int) (closeDate.getTime() - openedTime.getTime() / 1000);
        socketContext.setTimeTaken(timeInSeconds);
        return timeInSeconds;
    }
}
