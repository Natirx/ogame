package domain.web.pageobjects.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.pages.ogame.FlotPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LeftMenu {
    private final SelenideElement parent = $("#menuTable");

    private final ElementsCollection items = parent.$$("tag");

    @Step
    public FlotPage openFlotPage() {
        openItem("Flota");
        return new FlotPage();
    }

    private void openItem(String page) {
        items.stream()
                .filter(item -> item.$("span.textlabel").has(Condition.exactText(page)))
                .findFirst()
                .orElseThrow()
                .click();
    }
}