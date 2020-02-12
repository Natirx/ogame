package web.ogame;

import com.codeborne.selenide.WebDriverRunner;
import domain.web.featuresimpl.Feature;
import domain.web.featuresimpl.FeatureExecutor;
import domain.web.pageobjects.pages.ogame.HubPage;
import domain.web.pageobjects.pages.ogame.LoginPage;
import models.ogame.UserModel;
import web.BaseWebTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static data.SystemProperties.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SFBaseWebTest extends BaseWebTest {
    private final List<Feature> features = new ArrayList<>();

    protected SFBaseWebTest() {
    }

    public SFBaseWebTest add(Feature feature) {
        features.add(feature);
        return this;
    }

    public void trigger(){
        oepnOgame();
        WebDriverRunner.getWebDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        new FeatureExecutor(features).execute();
    }

    protected void oepnOgame() {
            assertThat(OG_URL)
                    .isNotNull();
            assertThat(OG_USERNAME)
                    .isNotNull();
            assertThat(OG_PASS)
                    .isNotNull();
        openOgameUrl(OG_URL);
            UserModel user = UserModel.builder()
                    .username(OG_USERNAME)
                    .password(OG_PASS)
                    .build();
            logInOgame(user);
            new HubPage().openLastGame();
        }

    protected void openOgameUrl(String url) {
        open(url);
    }

    protected void logInOgame(UserModel user) {
        new LoginPage()
                .setEmail(user.getUsername())
                .setPassword(user.getPassword())
                .login();
    }
}