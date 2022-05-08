package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import ru.netology.delivery.data.RegistrationDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest2DoubleSend {

    private RegistrationDto.RegistrationInfo registrationInfo;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        registrationInfo = RegistrationDto.getRegistrationInfo();
    }

    private static String getDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    public void shouldSendSimple() {
        String planningDate = getDate(4);
        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[class='notification__content']").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendDoubleDate() {

        String planningDate = getDate(4);
        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification'].notification_visible").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));

        planningDate = getDate(7);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $(withText("Запланировать")).click();
        $("[data-test-id='replan-notification'].notification_visible").shouldBe(Condition.visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

//        $("[data-test-id='replan-notification'] button").click();
        $(withText("Перепланировать")).click();
        $("[data-test-id='success-notification'].notification_visible").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }
}
