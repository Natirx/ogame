package web.ogame.flotpage;

import domain.web.featuresimpl.ogame.SAFE;
import org.testng.annotations.Test;
import web.ogame.SFBaseWebTest;

public class SAFEFLEET extends SFBaseWebTest {
    @Test
    public void test_main(){
        add(new SAFE()).trigger();
    }
}