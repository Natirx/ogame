package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.codeborne.selenide.Selenide.$;

public class PlanetChoose {
    private final SelenideElement parent = $("#fleet2"),
            galaxyInput = parent.$("#galaxy"),
            systemInput = parent.$("#system"),
            positionInput = parent.$("#position"),
            nextButton = parent.$("#continueToFleet3"),
            planetButton = parent.$("#pbutton"),
            moonButton = parent.$("#mbutton"),
            speedPanel = parent.$("#speedPercentage"),
            secondSpeed = speedPanel.$$("step ").get(1);


    @Step
    public PlanetChoose setToMoon() {
        moonButton.click();
        return this;
    }

    @Step
    public PlanetChoose setSecondSpeed() {
        secondSpeed.click();
        return this;
    }

    @Step
    public PlanetChoose setGalaxy(Integer galaxy) {

        galaxyInput.setValue(galaxy.toString());
        return this;
    }

    @Step
    public PlanetChoose setSystem(Integer system) {
        systemInput.setValue(system.toString());
        return this;
    }

    @Step
    public PlanetChoose setPosition(Integer position) {
        var system = systemInput.getValue();
        systemInput.setValue(Integer.toString(Integer.parseInt(system) + ThreadLocalRandom.current().nextInt(-2, 3)));
        positionInput.setValue(position.toString());
        return this;
    }


    @Step
    public PlanetChoose setPosition() {

        positionInput.setValue("16");
        return this;
    }

    @Step
    public MissionChoosePage next() {
        nextButton.click();
        return new MissionChoosePage();
    }
}
