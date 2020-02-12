package domain.web.pageobjects.overlays;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;

public abstract class AbstractOverlay {
    protected void clearField(SelenideElement element) {
        element.click();
        int length = element.getValue().length();
        for (int i = 0; i < length; i++) {
            Selenide.sleep(100);
            element.sendKeys(Keys.BACK_SPACE);
        }
    }

    protected void checkFieldValue(SelenideElement element, String value) {
        element.shouldBe(visible).shouldHave(value(value));
    }

    protected void checkSelectValue(SelenideElement select, String value) {
        select.shouldBe(visible).getSelectedOptions().find(selected).should(exactText(value));
    }

    protected void checkDatePickerValue(SelenideElement datePicker, LocalDate date) {
        datePicker.shouldBe(visible).shouldHave(value(date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))));
    }

    protected void checkOldDatePickerValue(SelenideElement datePicker, LocalDate date) {
        datePicker.shouldBe(visible).shouldHave(value(date.format(DateTimeFormatter.ofPattern("M/d/YYYY"))));
    }

    protected void verifyFieldDisabled(SelenideElement field) {
        field.shouldBe(visible).shouldHave(attribute("disabled"));
    }

    protected void verifyErrorMessage(SelenideElement element, String message) {
        Assertions.assertThat(element.closest("lightning-input").$x(".//div[@class='slds-form-element__help']").getText())
                .describedAs("Error message should be equals")
                .isEqualToIgnoringCase(message);
    }

    protected void verifyFieldErrorMessage(SelenideElement element, String message) {
        Assertions.assertThat(element.$x(".//div[@class='slds-form-element__help']").getText())
                .describedAs("Error message should be equals")
                .isEqualToIgnoringCase(message);
    }

    protected void verifyGroupedComboboxMessage(SelenideElement element, String message) {
        Assertions.assertThat(element.closest("lightning-grouped-combobox").$x(".//div[@class='slds-form-element__help']").getText())
                .describedAs("Error message should be equals")
                .isEqualToIgnoringCase(message);
    }

    protected void verifyRedBorder(SelenideElement element) {
        Assertions.assertThat(element.$x("//*[@class='slds-form-element__help']").getCssValue("border-color"))
                .describedAs("Input should be red")
                .isEqualTo("rgb(194, 57, 52)");
    }

    protected void verifyMenuListErrorMessage(SelenideElement element, String message) {
        Assertions.assertThat(element.find("ul.has-error > li").getText())
                .describedAs("Error message should be equals")
                .isEqualToIgnoringCase(message);
    }

    protected void verifyError(SelenideElement element) {
        verifyRedBorder(element);
        verifyFieldErrorMessage(element, "Complete this field.");
    }

    protected void verifyFieldEmpty(SelenideElement element) {
        element.shouldHave(empty);
    }
}
