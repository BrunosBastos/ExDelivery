package tqs.exdelivery.frontend;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.exdelivery.frontend.pages.ErrorMessage;
import tqs.exdelivery.frontend.pages.RegisterPage;

public class RegisterSteps {
  private WebDriver driver;
  private RegisterPage registerPage;
  private ErrorMessage errorMessage;

  @Before
  public void setUp() {
    WebDriverManager.firefoxdriver().setup();
    driver = new FirefoxDriver();
    registerPage = new RegisterPage(driver);
    errorMessage = new ErrorMessage(driver);
  }

  @Given("I am a Client trying to register")
  public void goToLoginPage() {
    registerPage.goTo();
  }

  @And("I insert a password like {string}")
  public void insertPassword(String password) {
    registerPage.insertPassword(password);
  }

  @And("I insert a name like {string}")
  public void insertName(String name) {
    registerPage.insertName(name);
  }

  @When("I insert an email like {string}")
  public void insertUsedEmail(String email) {
    registerPage.insertEmail(email);
  }

  @When("I insert an email in use like {string}")
  public void insertEmail(String usedEmail) {
    registerPage.insertEmail(usedEmail);
  }

  @And("I press the register button")
  public void pressRegisterButton() {
    registerPage.pressRegister();
  }

  @Then("A successfully registered message should appear")
  public void RegisterSuccessResponse() {
    errorMessage.checkSuccessMessage("Successfully registered");
    driver.quit();
  }

  @Then("A failed registered message should appear")
  public void RegisterErrorResponse() {
    errorMessage.checkSuccessMessage("Registration failed");
    driver.quit();
  }
}
