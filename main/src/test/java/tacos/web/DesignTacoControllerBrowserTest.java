package tacos.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.nio.file.Path.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesignTacoControllerBrowserTest {
    private static ChromeDriver browser;

    @LocalServerPort
    private int port;

    private String url;

    @BeforeAll
    static void openBrowser() {
        System.setProperty("webdriver.chrome.driver", of("src", "test", "resources", "chromedriver_78.exe").toString());
        browser = new ChromeDriver();
        browser
            .manage()
            .timeouts()
            .implicitlyWait(10, SECONDS);
    }

    @AfterAll
    static void closeBrowser() {
        browser.quit();
    }

    @BeforeEach
    void init() {
        url = "http://localhost:" + port;
    }

    @Test
    void testDesignATacoPage() {
        registerAndLoginUser();
        browser.get(url + "/design");

        List<WebElement> ingredientGroups = browser.findElementsByClassName("ingredient-group");
        assertEquals(5, ingredientGroups.size());

        WebElement wrapGroup = ingredientGroups.get(0);
        List<WebElement> wraps = wrapGroup.findElements(By.tagName("div"));
        assertEquals(2, wraps.size());
        assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
        assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");

        WebElement proteinGroup = ingredientGroups.get(1);
        List<WebElement> proteins = proteinGroup.findElements(By.tagName("div"));
        assertEquals(2, proteins.size());
        assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
        assertIngredient(proteinGroup, 1, "CARN", "Carnitas");
    }

    void registerAndLoginUser() {
        browser.get(url + "/register");
        browser.findElementById("username").sendKeys("user");
        browser.findElementById("password").sendKeys("pass");
        browser.findElementById("confirm").sendKeys("pass");
        browser.findElementById("register").click();
        browser.findElementById("username").sendKeys("user");
        browser.findElementById("password").sendKeys("pass");
        browser.findElementById("login").click();
    }

    private void assertIngredient(WebElement ingredientGroup,
                                  int ingredientIdx, String id, String name) {
        List<WebElement> proteins = ingredientGroup.findElements(By.tagName("div"));
        WebElement ingredient = proteins.get(ingredientIdx);
        assertEquals(id,
                ingredient.findElement(By.tagName("input")).getAttribute("value"));
        assertEquals(name,
                ingredient.findElement(By.tagName("span")).getText());
    }
}
