package domain.web.featuresimpl.ogame;

import com.codeborne.selenide.Selenide;
import domain.web.featuresimpl.Feature;
import domain.web.pageobjects.pages.ogame.FlotPage;
import utils.WaitUtils;

import java.time.Duration;

import static org.testng.Assert.fail;

public class SAFE extends OgameBaseFeature implements Feature {

    private FlotPage page;
    private Integer colonyIndex;


    @Override
    public void init() {
        page = openFlotPage();
    }

    @Override
    public void execute() {
        page.planetMenu.goToLastColony(4);
        WaitUtils.until(Duration.ofMillis(10000000), Duration.ofMillis(100), () -> {
            Selenide.refresh();
            if (page.fleet()) {
                page.selectAllFleet()
                        .next()
                        .setToMoon()
                        .next()
                        .getAllRes()
                        .setTransoprtMission()
                        .sendFleet();
            } else {
                fail("FAL");
            }
        });
    }
}