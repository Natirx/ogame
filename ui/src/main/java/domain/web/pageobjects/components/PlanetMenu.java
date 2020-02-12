package domain.web.pageobjects.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.pages.ogame.FlotPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PlanetMenu<T> {

    private T page;

    private final SelenideElement parent = $("#planetList"),
            lastColony = parent.$("#planet-33639493");

    private final ElementsCollection planets = parent.$$("div.smallplanet   ");

    @Step
    public FlotPage goToLastColony(Integer planetIndex) {
        planets.get(planetIndex - 1).click();
        return new FlotPage();
    }
}
