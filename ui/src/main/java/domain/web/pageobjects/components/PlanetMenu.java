package domain.web.pageobjects.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PlanetMenu<T> {

    private T page;

    private final SelenideElement parent = $("#planetList"),
        lastColony = parent.$("#planet-33639493");

    @Step
    public T goToLastColony(){
        lastColony.click();
        return page;
    }
}
