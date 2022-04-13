package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.RegistrationDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest3Error {
    private RegistrationDto.RegistrationInfo registrationInfo;

    String getDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        registrationInfo = RegistrationDto.getRegistrationInfo();
    }

    @Test
    public void shouldSendLetterError() {

        String planningDate = getDate(4);

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Семён Семёныч");
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

}
