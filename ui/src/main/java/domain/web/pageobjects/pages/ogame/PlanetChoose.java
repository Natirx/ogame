package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PlanetChoose {
    private final SelenideElement parent = $("#fleet2"),
            positionInput = parent.$("#position"),
            nextButton = parent.$("#continueToFleet3");

    @Step
    public PlanetChoose setPosition(){
        positionInput.setValue("16");
        return this;
    }

    @Step
    public MissionChoosePage next(){
        nextButton.click();
        return new MissionChoosePage();
    }
}
