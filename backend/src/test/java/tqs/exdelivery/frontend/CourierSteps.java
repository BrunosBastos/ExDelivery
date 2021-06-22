package tqs.exdelivery.frontend;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.exdelivery.frontend.pages.CourierPage;
import tqs.exdelivery.frontend.pages.ErrorMessage;
import tqs.exdelivery.frontend.pages.LoginPage;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierSteps {
    private final WebDriver driver;
    private final CourierPage courierPage;
    private final LoginPage loginPage;
    private final ErrorMessage errorMessage;

    public CourierSteps() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        loginPage = new LoginPage(driver);
        courierPage = new CourierPage(driver);
        errorMessage = new ErrorMessage(driver);
    }

    @Given("I am logged in as the platform admin and I am on couriers page")
    public void loggInAsOwner() {
        loginPage.loggInAs("leandro@gmail.com", "string");
        courierPage.goTo();
    }

    @Given("I see {int} couriers")
    public void iSeeCouriers(int numCouriers) {
        int numCouriersSeen = courierPage.getNumCouriers();
        assertThat(numCouriersSeen, is(numCouriers));
    }

    @Given("I see the courier {int} named {string}")
    public void iSeeTheCourier(int courierId, String name) {
        String nameSeen = courierPage.getCourierName(courierId);
        assertThat(nameSeen, containsString(name));
    }

    @When("I press the courier {int} fire button")
    public void iFireCourier(int courierId) {
        courierPage.fireCourier(courierId);
    }

    @Then("A successfully fired this courier message should appear")
    public void fireSuccessResponse() {
        errorMessage.checkSuccessMessage("Successfully fired this courier.");
    }

    @After
    public void afterScenario() {
        driver.quit();
    }
}
