import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;


public class RGSTest {

    public WebDriver webDriver;
    private WebDriverWait wait;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "webdriver/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");

        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(webDriver, 10, 1000);

        String url = "https://www.rgs.ru/";
        webDriver.get(url);
    }

    @After
    public void close() {
        webDriver.quit();
    }

    @Test
    public void rgsTest() {

        //Кликнуть по "Меню"
        WebElement menu = webDriver.findElement(By.xpath("//a[@data-toggle='dropdown'][contains(text(),'Меню')]"));
        menu.click();

        //Кликнуть по "Компаниям"
        WebElement forCompany = menu.findElement(By.xpath("//a[contains(text(), 'Компаниям')]"));
        forCompany.click();

        Assert.assertEquals("Корпоративное страхование для компаний | Росгосстрах", webDriver.getTitle());

        //Кликнуть по "Здоровье"
        WebElement health = webDriver.findElement(By.xpath("//a[contains(text(), 'Здоровье')][contains(@class,'list')]"));
        health.click();

        Assert.assertEquals("ДМС для сотрудников - добровольное медицинское страхование от Росгосстраха", webDriver.getTitle());

        //Кликнуть по "ДМС"
        WebElement DMS = webDriver.findElement(By.xpath("//a[contains(text(), 'Добровольное')]"));
        DMS.click();

        Assert.assertEquals("Добровольное медицинское страхование в Росгосстрахе", webDriver.getTitle());

        //Кликнуть по "Отправить заявку"
        WebElement send = webDriver.findElement(By.xpath("//a[contains(text(), 'Отправить заявку')]"));
        send.click();

        //Поиск полей
        WebElement lastName = webDriver.findElement(By.xpath("//input[@name='LastName']"));
        WebElement firstName = webDriver.findElement(By.xpath("//input[@name='FirstName']"));
        WebElement email = webDriver.findElement(By.xpath("//input[@name='Email']"));
        WebElement phone = webDriver.findElement(By.xpath("//input[contains(@data-bind, 'Phone')]"));
        Select select = new Select(webDriver.findElement(By.xpath("//select")));
        WebElement contactDate = webDriver.findElement(By.xpath("//input[@name='ContactDate']"));
        WebElement checkBox = webDriver.findElement(By.xpath("//input[@class='checkbox']"));
        WebElement sendButton = webDriver.findElement(By.xpath("//button[contains(text(), 'Отправить')]"));

        //Заполнение полей
        fillInputField(lastName, "Фамилия");
        fillInputField(firstName, "Имя");
        fillInputField(email, "qwertyqwerty");
        fillInputField(phone, "9999999999");
        select.selectByVisibleText("Москва");
        fillInputField(contactDate, "03032021");
        checkBox.click();
        sendButton.click();

        //Проверка
        checkErrorMessageAtField(email, "Введите адрес электронной почты");

    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webDriver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        element.click();
        element.sendKeys(value);
    }

    private void checkErrorMessageAtField(WebElement element, String errorMessage) {
        element = element.findElement(By.xpath("./..//span"));
        waitUtilElementToBeVisible(element);
        Assert.assertEquals("Проверка ошибки у поля не была пройдена",
                errorMessage, element.getText());
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}
