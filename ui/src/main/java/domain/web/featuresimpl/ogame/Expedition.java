package domain.web.featuresimpl.ogame;

import domain.web.featuresimpl.Feature;
import domain.web.pageobjects.pages.ogame.FlotPage;

public class Expedition extends OgameBaseFeature implements Feature {

    FlotPage page;

    @Override
    public void init() {
        page = openFlotPage();
    }

    @Override
    public void execute() {
        Integer count = page.getAvailableExp() - page.getFreeExp();
        if (count != 0) {
            page.planetMenu
                    .goToLastColony(3)
                    .setSmallTransporters(count)
                    .setLargeTransporters(count)
                    .setEspionage()
                    .setHeavy()
                    .next() 
                    .setPosition()
                    .next()
                    .setExpMission()
                    .sendFleet();
        }
    }
}