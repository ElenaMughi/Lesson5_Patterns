import com.codeborne.selenide.Condition;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest2 {

    private static Faker faker;

    String getDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeAll
    static void setUpAll() {
        faker = new Faker(new Locale("ru"));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");  // вопрос: open("http://0.0.0.0:9999");
    }

    @Test
    public void shouldSendSimple() {

        String planningDate = getDate(7);
        String sity = faker.address().cityName();
        $("[data-test-id='city'] input").setValue(sity);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(faker.name().fullName());
        $("[data-test-id='phone'] input").setValue(faker.phoneNumber().phoneNumber());
        $("[data-test-id='agreement']").click();

        $(withText("Запланировать")).click();
        $("[class='notification__content']").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldSendDoubleDate() {

        String planningDate = getDate(4);
        String sity = faker.address().cityName();
        $("[data-test-id='city'] input").setValue(sity);
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue(faker.name().fullName());
        $("[data-test-id='phone'] input").setValue(faker.phoneNumber().phoneNumber());
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
