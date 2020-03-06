package domain.web.featuresimpl.ogame;

import domain.web.featuresimpl.Feature;
import domain.web.pageobjects.pages.ogame.FlotPage;
import domain.web.pageobjects.pages.ogame.PlanetChoose;

import java.util.Map;

public class FleetHide extends OgameBaseFeature implements Feature {

    private FlotPage page;

    @Override
    public void init() {
        page = openFlotPage();
    }

    @Override
    public void execute() {
        Map<String, String> coordinates = page.topMenu.getPlanetCoordinates();

        coordinates.forEach((k, v) -> {
            PlanetChoose planetChoose = page.planetMenu
                    .goToCoordinates(v, k)
                    .selectAllFleet()
                    .next();
            if (v.equals("[4:190:9]")) {
                planetChoose
                        .setGalaxy(1)
                        .setSystem(6)
                        .setPosition(4)
                        .setToMoon()
                        .setSecondSpeed()
                        .next()
                        .setTransoprtMission()
                        .getAllRes()
                        .sendFleet();
            }
        });
    }
}