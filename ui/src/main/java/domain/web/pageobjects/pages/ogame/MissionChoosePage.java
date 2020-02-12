package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class MissionChoosePage {
    private final SelenideElement parent = $("#fleet3"),
            mission = parent.$("#missions"),
            sendFleet = parent.$("#sendFleet");


    @Step
    public MissionChoosePage setExpMission() {
        mission.$("#missionButton15").click();
        return this;
    }

    @Step
    public void sendFleet() {
        sendFleet.click();
    }
}
