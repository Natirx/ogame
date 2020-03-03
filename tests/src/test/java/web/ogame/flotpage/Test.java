package web.ogame.flotpage;

import domain.web.featuresimpl.ogame.Expedition;
import web.ogame.SFBaseWebTest;

public class Test extends SFBaseWebTest {
    @org.testng.annotations.Test(invocationCount = 5)
    public void test_main() {
        add(new Expedition(6)).trigger();
    }
}