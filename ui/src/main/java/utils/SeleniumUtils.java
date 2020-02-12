package utils;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.interactions.Actions;

public class SeleniumUtils {

    public static void dragAndDrop(SelenideElement from, SelenideElement to){
        new Actions(WebDriverRunner.getWebDriver())
                .clickAndHold((from.scrollIntoView(false)))
                .moveToElement(to)
                .click(to).perform();
    }
}
