package web.ogame.flotpage;

import domain.web.featuresimpl.ogame.FleetHide;
import org.testng.annotations.Test;
import web.ogame.SFBaseWebTest;

public class HideFleetTest extends SFBaseWebTest {
    @Test
    public void test_main(){
        add(new FleetHide()).trigger();
    }
}
