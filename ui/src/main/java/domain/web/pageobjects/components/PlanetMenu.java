package domain.web.pageobjects.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PlanetMenu<T> {

    private T page;

    private final SelenideElement parent = $("#planetList"),
            lastColony = parent.$("#planet-33639493");

    private final ElementsCollection planets = parent.$$("idv.smallplanet   ");

    @Step
    public T goToLastColony(Integer planetIndex) {
        planets.get(planetIndex - 1).click();
        return page;
    }
}
