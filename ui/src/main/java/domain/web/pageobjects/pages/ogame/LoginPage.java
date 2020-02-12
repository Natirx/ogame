package domain.web.pageobjects.pages.ogame;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import domain.web.pageobjects.pages.AbstractPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends AbstractPage {

    private final static String LOGIN = "Login";

    private final SelenideElement parent = $("#loginRegisterTabs"),
            loginTab = parent.$$("span").find(Condition.exactText(LOGIN)),
            loginInput = parent.$("input[name='email']"),
            passwordInput = parent.$("input[name='password']"),
            submitButton = parent.$("button[type='submit']");

    @Step
    public LoginPage openLoginTab() {
        loginTab.click();
        return this;
    }

    @Step
    public LoginPage setEmail(String email) {
        loginInput.setValue(email);
        return this;
    }

    @Step
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step
    public void login() {
        submitButton.click();
    }
}