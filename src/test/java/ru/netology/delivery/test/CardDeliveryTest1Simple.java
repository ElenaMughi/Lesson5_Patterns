package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.RegistrationDto;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


import java.time.Duration;

public class CardDeliveryTest1Simple {

    private RegistrationDto.RegistrationInfo registrationInfo;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }


    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
        registrationInfo = RegistrationDto.getRegistrationInfo();
    }

    @Test
    public void shouldSendSimple() {

        String planningDate = RegistrationDto.getDate(4);

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendWithDash() {

        String planningDate = RegistrationDto.getDate(30);
        $("[data-test-id='city'] input").setValue("Ростов-на-Дону");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Москвич-Рязанский");
        $("[data-test-id='phone'] input").setValue("+79998887744");
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendWithSpace() {
        String planningDate = RegistrationDto.getDate(3);

        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Испански Марк Аврелий");
        $("[data-test-id='phone'] input").setValue("+49998887744");
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendPhoneNotPlus() { //теперь позитивный тест
        String planningDate = RegistrationDto.getDate(7);

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Египетский Марк Аврелий");
        $("[data-test-id='phone'] input").setValue("71112223344");
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendPhoneManyNumbers() { //теперь позитивный тест
        String planningDate = RegistrationDto.getDate(14);

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Греческий Марк Аврелий");
        $("[data-test-id='phone'] input").setValue("+211122233448");
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    // негативные тесты

    @Test
    public void shouldSendCityEmpty() {

        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='city'] span").shouldHave(Condition.text("Поле обязательно для заполнения"));

    }

    @Test
    public void shouldSendCityEng() {

        $("[data-test-id='city'] input").setValue("Moscow");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='city'] span").shouldHave(Condition.text("Доставка в выбранный город недоступна"));

    }

    @Test
    public void shouldSendCityNotAdmCenter() {

        $("[data-test-id='city'] input").setValue("Реутов");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='city'] span").shouldHave(Condition.text("Доставка в выбранный город недоступна"));

    }

    @Test
    public void shouldSendWithoutData() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='date'] span").shouldHave(Condition.text("Неверно введена дата"));
    }

    @Test
    public void shouldSendTodayData() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(0));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='date'] span").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldSendTodayPlus2() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(2));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='date'] span").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldSendFIOEmpty() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'] span").shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldSendFIOEng() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue("Ivanov");
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'] span")
                .shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldSendFIONumber() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue("Николай 2");
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='name'] span")
                .shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldSendPhoneEmpty() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue("");
        $(withText("Запланировать")).click();
        $("[data-test-id='phone'] span")
                .shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldSendWithoutAgreement() {

        $("[data-test-id='city'] input").setValue(registrationInfo.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(RegistrationDto.getDate(3));
        $("[data-test-id='name'] input").setValue(registrationInfo.getName());
        $("[data-test-id='phone'] input").setValue(registrationInfo.getPhone());
        $(withText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
