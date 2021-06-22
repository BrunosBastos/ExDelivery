package tqs.exdelivery.frontend;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.exdelivery.frontend.pages.LoginPage;
import tqs.exdelivery.frontend.pages.PlatformDeliveriesPage;


public class PlatformDeliveriesSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private PlatformDeliveriesPage platformDeliveriesPage;

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        loginPage = new LoginPage(driver);
        platformDeliveriesPage = new PlatformDeliveriesPage(driver);
    }

    @Given("I am logged in as the platform admin and I am on platform deliveries page")
    public void goToPlatformDeliveriesPage() {
        loginPage.loggInAs("leandro@gmail.com", "string");
        platformDeliveriesPage.goTo();
    }

    @When("I am able to see a few deliveries")
    public void iAmAbleToSeeAFewDeliveries() {
        platformDeliveriesPage.checkDeliveriesMoreThanOne();
    }

    @And("I press the search button")
    public void iPressTheSearchButton() {
        platformDeliveriesPage.pressSearchButton();
    }

    @Then("I press the first one and I am redirected to delivery details page")
    public void iPressTheFirstOneAndIAmRedirectedToDeliveryDetailsPage() {
        platformDeliveriesPage.pressFirstOne();
    }

    @And("I change the ordering option to {string}")
    public void iChangeTheOrderingOptionToDescending(String sortOrder) {
        platformDeliveriesPage.insertOrder(sortOrder);
    }

    @And("I search for a courier with the email {string}")
    public void iSearchForACourierWithTheEmailTiagoGmailCom(String email) {
        platformDeliveriesPage.insertCourierEmail(email);
    }
}
