package domain.web.pageobjects.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.pages.ogame.FlotPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PlanetMenu {


    private final SelenideElement parent = $("#planetList"),
            lastColony = parent.$("#planet-33639493");

    private final ElementsCollection planets = parent.$$("div.smallplanet   "),
            coordinatesValue = parent.$$("span.planet-koords "),
            moons = parent.$$("a.moonlink  ");

    @Step
    public FlotPage goToLastColony(Integer planetIndex) {
        planets.get(planetIndex - 1).click();
        return new FlotPage();
    }

    @Step
    public FlotPage goToCoordinates(String coordinates, String name) {
        if (name.equals("Planeta")) {
            coordinatesValue.find(Condition.exactText(coordinates)).click();
        } else {
            coordinatesValue.find(Condition.exactText(coordinates)).closest("div").$(".moonlink  ").click();
        }
        return new FlotPage();
    }
}
