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
        import tqs.exdelivery.frontend.pages.CourierDeliveriesPage;

public class CourierDeliveriesSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private CourierDeliveriesPage courierDeliveriesPage;

    @Before
    public void setUp() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        loginPage = new LoginPage(driver);
        courierDeliveriesPage = new CourierDeliveriesPage(driver);
    }

    @Given("I am logged in as a courier and I am on the deliveries page")
    public void goToCourierDeliveriesPage() {
        loginPage.loggInAs("tiago@gmail.com", "string");
        courierDeliveriesPage.goTo();
    }

    @When("I am able to see all my deliveries")
    public void iAmAbleToSeeAFewDeliveries() {
        courierDeliveriesPage.checkDeliveriesMoreThanOne();
    }

    @Then("I press my latest one and I am redirected to delivery details page")
    public void iPressTheFirstOneAndIAmRedirectedToDeliveryDetailsPage() {
        courierDeliveriesPage.pressFirstOne();
    }

    @And("I change the ordering of my deliveries to {string}")
    public void iChangeTheOrderingOptionToDescending(String sortOrder) {
        courierDeliveriesPage.insertOrder(sortOrder);
    }

    @And("I am able to see a delivery status")
    public void checkStatusOfADelivery() {
        courierDeliveriesPage.checkStatusOfADelivery();
    }
}
