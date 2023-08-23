package socket;

import lombok.NoArgsConstructor;

import java.net.URISyntaxException;
import java.util.Map;

@NoArgsConstructor
public class WebClient {
    private Client client;

    public static WebClient getInstance() {
        return new WebClient();
    }

    public void connectToSocket(SocketContext context) throws URISyntaxException, InterruptedException {
        boolean isBodySent = false;

        try {
            client = new Client(context);
            if (!context.getRequestHeaders().isEmpty()) {
                final Map<String, String> requestHeaderParams = context.getRequestHeaders();
                requestHeaderParams.forEach((k, v) -> {
                    client.addHeader(k, v);
                });
                client.connectBlocking();
                while (!client.isClosing()) {
                    if (client.getAliveTime() >= context.getTimeOut()) {
                        client.close(1006, "Time out");
                    }
                    if (context.getRunnable() != null) {
                        context.getRunnable().run();
                    }
                    if (context.getBody() != null && !isBodySent) {
                        client.send(context.getBody());
                        isBodySent = true;
                    }
                    if (context.getExpectedMessage() != null) {
                        client.onMessage(context.getExpectedMessage());
                        return;
                    }
                }
            }
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
