package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.components.PlanetMenu;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class FlotPage {
    private final SelenideElement parent = $("#fleet1"),
            fleetStatusSection = parent.$("#slots"),
            expBar = fleetStatusSection.$$("span").find(Condition.exactText("Ekspedycje:")),
            civilSection = parent.$("#civil"),
            battleshipsSection = parent.$("#battleships"),
            fighterHeavy = battleshipsSection.$("li.fighterHeavy"),
            fighterHeavyInput = fighterHeavy.$("input"),
            fighterHeavyCount = fighterHeavy.$("span.amount"),
            smallTransportersSection = civilSection.$("li.transporterSmall"),
            smallTransportersCount = smallTransportersSection.$("span.amount"),
            smallTransportersInput = smallTransportersSection.$("input"),
            largeTransportersSection = civilSection.$("li.transporterLarge"),
            largeTransportersCount = largeTransportersSection.$("span.amount"),
            largeTransportersInput = largeTransportersSection.$("input"),
            espionageProbeSection = civilSection.$("li.espionageProbe"),
            espionageProbeInput = espionageProbeSection.$("input"),
            espionageProbeCount = espionageProbeSection.$("span.amount"),
            nextButton = parent.$("#continueToFleet2"),
            explorerSection = battleshipsSection.$("li.explorer"),
            explorerInput = explorerSection.$("input"),
            explorerCount = explorerSection.$("span.amount");


    public PlanetMenu<FlotPage> planetMenu = new PlanetMenu<>();

    @Step
    public FlotPage setSmallTransporters(Integer expCount) {
        Integer stCount = numberToSet(expCount, getSmallTransportersCount());
        smallTransportersInput.setValue(stCount.toString());
        return this;
    }

    @Step
    public FlotPage setLargeTransporters(Integer expCount) {
        Integer stCount = numberToSet(expCount, getLargeTransportersCount());
        largeTransportersInput.setValue(stCount.toString());
        return this;
    }

    @Step
    public FlotPage setEspionage() {
        if (Integer.parseInt(espionageProbeCount.getText().replace(".", "")) != 0) {
            espionageProbeInput.setValue("1");
        }
        return this;
    }

    @Step
    public FlotPage setHeavy() {
        if (Integer.parseInt(fighterHeavyCount.getText().replace(".", "")) != 0) {
            fighterHeavyInput.setValue("1");
        }
        return this;
    }

    @Step
    public FlotPage setExpoler() {
        if (Integer.parseInt(explorerCount.getText().replace(".", "")) != 0) {
            explorerInput.setValue("1");
        }
        return this;
    }

    @Step
    public PlanetChoose next() {
        nextButton.click();
        return new PlanetChoose();
    }

    private Integer numberToSet(Integer expCount, String count) {
        Integer stCount = Integer.parseInt(count.replace(".", ""));
        Integer stToSet;
        if (expCount != 1) {
            stToSet = stCount / expCount;
        } else {
            stToSet = stCount;
        }
        return Math.round(stToSet);
    }

    private String getSmallTransportersCount() {
        return smallTransportersCount.getText();
    }

    private String getLargeTransportersCount() {
        return largeTransportersCount.getText();
    }

    @Step
    public Integer getFreeExp() {
        return Integer.parseInt(getExpCount().substring(12, 13));
    }

    @Step
    public Integer getAvailableExp() {
        return Integer.parseInt(getExpCount().substring(14));
    }

    private String getExpCount() {
        return expBar.parent().getText();
    }
}