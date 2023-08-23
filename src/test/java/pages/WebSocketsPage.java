package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class WebSocketsPage {
    private final String urlForm = $x("//input[@id='txtServerUrl']").getValue();
    private final SelenideElement connectBtn = $x("//button[text()='Connect']");
}
