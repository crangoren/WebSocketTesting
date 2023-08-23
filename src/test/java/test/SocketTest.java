package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import socket.SocketContext;
import socket.WebClient;

import java.net.URISyntaxException;

import static com.codeborne.selenide.Selenide.$x;

public class SocketTest {
    private SocketContext context;

    @Test
    public void socketUI_ReceiveText() throws URISyntaxException, InterruptedException {
//        Selenide.open("https://socketsbay.com/test-websockets"); ------------------
//         String urlForm = $x("//input[@id='txtServerUrl']").getValue(); ------------------
//         $x("//button[text()='Connect']").click(); ------------------
         Selenide.open("https://www.piesocket.com/websocket-tester");
         String url = $x("//input[@id='email']").getValue();
         $x("//button[@type='submit']").click();
         String expectedMessage = "testmessage";
         context = new SocketContext();
         context.setURI(url);
         context.setBody(expectedMessage);
         context.setExpectedMessage(expectedMessage);
         context.setTimeOut(5);

        WebClient.getInstance().connectToSocket(context);
//        $x("//pre[@id='log']") ----------
//        $x("//*[@id='consoleLog']")
//                .shouldHave(Condition.partialText(context.getBody()));
    }

    @Test
    public void socketUI_SendText() throws URISyntaxException, InterruptedException {
        context = new SocketContext();
        Selenide.open("https://www.piesocket.com/websocket-tester");
        SelenideElement input = $x("//input[@id='email']");
        SelenideElement button = $x("//button[@type='submit']");
        String expectedMessage = "testmessage";

        String url = input.getValue();
        button.click();

        Runnable sendUIMessage = new Runnable() {
            @Override
            public void run() {
                input.clear();
                input.sendKeys(expectedMessage);
                button.click();
            }
        };

        context.setURI(url);
        context.setExpectedMessage(expectedMessage);
        context.setTimeOut(5);
        context.setRunnable(sendUIMessage);

        WebClient.getInstance().connectToSocket(context);
    }
}
