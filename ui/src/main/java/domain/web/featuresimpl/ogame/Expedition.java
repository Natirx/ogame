package domain.web.featuresimpl.ogame;

import domain.web.featuresimpl.Feature;
import domain.web.pageobjects.pages.ogame.FlotPage;

public class Expedition extends OgameBaseFeature implements Feature {

    FlotPage page;
    private Integer colonyIndex;

    public Expedition(Integer colonyIndex) {
        this.colonyIndex = colonyIndex;
    }

    @Override
    public void init() {
        page = openFlotPage();
    }

    @Override
    public void execute() {
        Integer count = page.getAvailableExp() - page.getFreeExp();
        if (count != 0) {
            page.planetMenu
                    .goToLastColony(colonyIndex)
                    .setSmallTransporters(count)
                    .setLargeTransporters(count)
                    .setEspionage()
                    .setHeavy()
                    .setExpoler()
                    .setInterseptor()
                    .setDestroyer()
                    .next() 
                    .setPosition()
                    .next()
                    .setExpMission()
                    .sendFleet();
        }
    }
}