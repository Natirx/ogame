package domain.web.featuresimpl.ogame;

        import domain.web.pageobjects.components.LeftMenu;
        import domain.web.pageobjects.pages.ogame.FlotPage;

class OgameBaseFeature {
    FlotPage openFlotPage() {
        return new LeftMenu().openFlotPage();
    }
}
