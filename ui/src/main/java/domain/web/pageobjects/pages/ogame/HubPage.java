package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.pages.AbstractPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class HubPage extends AbstractPage {

    private final SelenideElement parent = $("#hub"),
            joinGameSection = parent.$("#joinGame"),
            lastGameButton = joinGameSection.$$("button").last();

    @Step
    public void openLastGame() {
        lastGameButton.click();
        Selenide.switchTo().window(1);
    }
}