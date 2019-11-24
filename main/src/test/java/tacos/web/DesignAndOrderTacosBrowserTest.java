package tacos.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.By.tagName;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DesignAndOrderTacosBrowserTest {
    private static Browser browser;
    private static boolean initialized;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void setup() {
        browser = new Browser();
    }

    @AfterAll
    static void closeBrowser() {
        browser.quit();
    }

    @BeforeEach
    void setUp() {
        if (!initialized) {
            browser.setPort(port);
            initialized = true;
        }
    }

    @Test
    void testDesignATacoPage_HappyPath() {
        browser.get();
        browser.clickDesignATaco();
        browser.assertLandedOnLoginPage();
        browser.doRegistration("testuser", "testpassword");
        browser.assertLandedOnLoginPage();
        browser.doLogin("testuser", "testpassword");
        browser.assertDesignPageElements();
        browser.buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        browser.clickBuildAnotherTaco();
        browser.buildAndSubmitATaco("Another Taco", "COTO", "CARN", "JACK", "LETC", "SRCR");
        browser.fillInAndSubmitOrderForm();
        browser.assertUrlIsCurrent();
        browser.doLogout();
    }

    @Test
    void testDesignATacoPage_EmptyOrderInfo() {
        browser.get();
        browser.clickDesignATaco();
        browser.assertLandedOnLoginPage();
        browser.doRegistration("testuser2", "testpassword");
        browser.doLogin("testuser2", "testpassword");
        browser.assertDesignPageElements();
        browser.buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        browser.submitEmptyOrderForm();
        browser.fillInAndSubmitOrderForm();
        browser.assertUrlIsCurrent();
        browser.doLogout();
    }

    @Test
    void testDesignATacoPage_InvalidOrderInfo() {
        browser.get();
        browser.clickDesignATaco();
        browser.assertLandedOnLoginPage();
        browser.doRegistration("testuser3", "testpassword");
        browser.doLogin("testuser3", "testpassword");
        browser.assertDesignPageElements();
        browser.buildAndSubmitATaco("Basic Taco", "FLTO", "GRBF", "CHED", "TMTO", "SLSA");
        browser.submitInvalidOrderForm();
        browser.fillInAndSubmitOrderForm();
        browser.assertUrlIsCurrent();
        browser.doLogout();
    }

    private static class Browser {
        private final HtmlUnitDriver driver = new HtmlUnitDriver();
        private String url;
        private String registrationPageUrl;
        private String orderDetailsPageUrl;
        private String loginPageUrl;
        private String designPageUrl;
        private String currentOrderDetailsPageUrl;

        void setPort(int port) {
            url = "http://localhost:" + port + "/";
            registrationPageUrl = url + "register";
            orderDetailsPageUrl = url + "orders";
            loginPageUrl = url + "login";
            designPageUrl = url + "design";
            currentOrderDetailsPageUrl = url + "orders/current";
        }

        {
            driver
                .manage()
                .timeouts()
                .implicitlyWait(10, SECONDS);
        }

        void get() {
            driver.get(url);
        }

        void quit() {
            driver.quit();
        }

        void clickDesignATaco() {
            assertEquals(url, getCurrentUrl());
            driver.findElementByCssSelector("a[id='design']").click();
        }

        private String getCurrentUrl() {
            return driver.getCurrentUrl();
        }

        private void clickBuildAnotherTaco() {
            assertTrue(getCurrentUrl().startsWith(orderDetailsPageUrl));
            driver.findElementByCssSelector("a[id='another']").click();
        }

        private void doRegistration(String username, String password) {
            driver.findElementByLinkText("here").click();
            assertEquals(registrationPageUrl, getCurrentUrl());
            driver.findElementByName("username").sendKeys(username);
            driver.findElementByName("password").sendKeys(password);
            driver.findElementByName("confirm").sendKeys(password);
            driver.findElementByName("fullname").sendKeys("Test McTest");
            driver.findElementByName("street").sendKeys("1234 Test Street");
            driver.findElementByName("city").sendKeys("Testville");
            driver.findElementByName("state").sendKeys("TX");
            driver.findElementByName("zip").sendKeys("12345");
            driver.findElementByName("phone").sendKeys("123-123-1234");
            driver.findElementByCssSelector("form#registerForm").submit();
        }

        private void buildAndSubmitATaco(String name, String... ingredients) {
            assertDesignPageElements();

            for (String ingredient : ingredients) {
                driver.findElementByCssSelector("input[value='" + ingredient + "']").click();
            }
            driver.findElementByCssSelector("input#name").sendKeys(name);
            driver.findElementByCssSelector("form#tacoForm").submit();
        }

        private void assertDesignPageElements() {
            assertEquals(designPageUrl, getCurrentUrl());
            List<WebElement> ingredientGroups = driver.findElementsByClassName("ingredient-group");
            assertEquals(5, ingredientGroups.size());

            WebElement wrapGroup = driver.findElementByCssSelector("div.ingredient-group#wraps");
            List<WebElement> wraps = wrapGroup.findElements(tagName("div"));
            assertEquals(2, wraps.size());
            assertIngredient(wrapGroup, 0, "FLTO", "Flour Tortilla");
            assertIngredient(wrapGroup, 1, "COTO", "Corn Tortilla");

            WebElement proteinGroup = driver.findElementByCssSelector("div.ingredient-group#proteins");
            List<WebElement> proteins = proteinGroup.findElements(tagName("div"));
            assertEquals(2, proteins.size());
            assertIngredient(proteinGroup, 0, "GRBF", "Ground Beef");
            assertIngredient(proteinGroup, 1, "CARN", "Carnitas");

            WebElement cheeseGroup = driver.findElementByCssSelector("div.ingredient-group#cheeses");
            List<WebElement> cheeses = proteinGroup.findElements(tagName("div"));
            assertEquals(2, cheeses.size());
            assertIngredient(cheeseGroup, 0, "CHED", "Cheddar");
            assertIngredient(cheeseGroup, 1, "JACK", "Monterrey Jack");

            WebElement veggieGroup = driver.findElementByCssSelector("div.ingredient-group#veggies");
            List<WebElement> veggies = proteinGroup.findElements(tagName("div"));
            assertEquals(2, veggies.size());
            assertIngredient(veggieGroup, 0, "TMTO", "Diced Tomatoes");
            assertIngredient(veggieGroup, 1, "LETC", "Lettuce");

            WebElement sauceGroup = driver.findElementByCssSelector("div.ingredient-group#sauces");
            List<WebElement> sauces = proteinGroup.findElements(tagName("div"));
            assertEquals(2, sauces.size());
            assertIngredient(sauceGroup, 0, "SLSA", "Salsa");
            assertIngredient(sauceGroup, 1, "SRCR", "Sour Cream");
        }

        private void assertIngredient(WebElement ingredientGroup,
                                      int ingredientIdx,
                                      String id,
                                      String name) {
            var ingredient = ingredientGroup
                                        .findElements(tagName("div"))
                                        .get(ingredientIdx);
            assertEquals(id,
                    ingredient
                            .findElement(tagName("input"))
                            .getAttribute("value"));
            assertEquals(name,
                    ingredient
                            .findElement(tagName("span"))
                            .getText());
        }

        private void submitInvalidOrderForm() {
            assertTrue(getCurrentUrl().startsWith(orderDetailsPageUrl));
            fillField("input#name", "I");
            fillField("input#street", "1");
            fillField("input#city", "F");
            fillField("input#state", "C");
            fillField("input#zip", "8");
            fillField("input#ccNumber", "1234432112344322");
            fillField("input#ccExpiration", "14/91");
            fillField("input#ccCVV", "1234");
            driver.findElementByCssSelector("form#orderForm").submit();

            assertEquals(orderDetailsPageUrl, getCurrentUrl());

            List<String> validationErrors = getValidationErrorTexts();
            assertEquals(4, validationErrors.size());
            assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
            assertTrue(validationErrors.contains("Not a valid credit card number"));
            assertTrue(validationErrors.contains("Must be formatted MM/YY"));
            assertTrue(validationErrors.contains("Invalid CVV"));
        }

        private void fillField(String fieldName, String value) {
            WebElement field = driver.findElementByCssSelector(fieldName);
            field.clear();
            field.sendKeys(value);
        }

        private List<String> getValidationErrorTexts() {
            return driver
                    .findElementsByClassName("validationError")
                    .stream()
                    .map(WebElement::getText)
                    .collect(toList());
        }

        private void assertLandedOnLoginPage() {
            assertEquals(loginPageUrl, getCurrentUrl());
        }

        private void fillInAndSubmitOrderForm() {
            assertTrue(getCurrentUrl().startsWith(orderDetailsPageUrl));
            fillField("input#name", "Ima Hungry");
            fillField("input#street", "1234 Culinary Blvd.");
            fillField("input#city", "Foodsville");
            fillField("input#state", "CO");
            fillField("input#zip", "81019");
            fillField("input#ccNumber", "4111111111111111");
            fillField("input#ccExpiration", "10/19");
            fillField("input#ccCVV", "123");
            driver.findElementByCssSelector("form#orderForm").submit();
        }

        void doLogin(String username, String password) {
            driver.findElementByName("username").sendKeys(username);
            driver.findElementByName("password").sendKeys(password);
            driver.findElementByCssSelector("form#loginForm").submit();
        }

        void assertUrlIsCurrent() {
            assertEquals(url, getCurrentUrl());
        }

        void doLogout() {
            ofNullable(driver.findElementByCssSelector("form#logoutForm"))
                    .ifPresent(WebElement::submit);
        }

        void submitEmptyOrderForm() {
            assertEquals(currentOrderDetailsPageUrl, getCurrentUrl());
            // clear fields automatically populated from user profile
            fillField("input#name", "");
            fillField("input#street", "");
            fillField("input#city", "");
            fillField("input#state", "");
            fillField("input#zip", "");
            driver.findElementByCssSelector("form#orderForm").submit();

            assertEquals(orderDetailsPageUrl, getCurrentUrl());

            List<String> validationErrors = getValidationErrorTexts();
            assertEquals(9, validationErrors.size());
            assertTrue(validationErrors.contains("Please correct the problems below and resubmit."));
            assertTrue(validationErrors.contains("Name is required"));
            assertTrue(validationErrors.contains("Street is required"));
            assertTrue(validationErrors.contains("City is required"));
            assertTrue(validationErrors.contains("State is required"));
            assertTrue(validationErrors.contains("Zip code is required"));
            assertTrue(validationErrors.contains("Not a valid credit card number"));
            assertTrue(validationErrors.contains("Must be formatted MM/YY"));
            assertTrue(validationErrors.contains("Invalid CVV"));
        }
    }
}
