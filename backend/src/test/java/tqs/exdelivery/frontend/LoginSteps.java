package tqs.exdelivery.frontend;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqs.exdelivery.frontend.pages.ErrorMessage;
import tqs.exdelivery.frontend.pages.LoginPage;

public class LoginSteps {
  private final WebDriver driver;
  private final LoginPage loginPage;
  private final ErrorMessage errorMessage;

  public LoginSteps() {
    WebDriverManager.firefoxdriver().setup();
    driver = new FirefoxDriver();
    loginPage = new LoginPage(driver);
    errorMessage = new ErrorMessage(driver);
  }

  @Given("I am a Client trying to log in")
  public void goToLoginPage() {
    loginPage.goTo();
  }

  @When("I insert my email {string}")
  public void insertEmail(String email) {
    loginPage.insertEmail(email);
  }

  @And("I insert my correct password")
  public void insertCorrectPassword() {
    loginPage.insertPassword("string");
  }

  @And("I insert a wrong password")
  public void insertIncorrectPassword() {
    loginPage.insertPassword("password");
  }

  @And("I press the login button")
  public void pressLogin() {
    loginPage.pressLogin();
  }

  @Then("A successfully logged in message should appear")
  public void loginSuccessResponse() {
    errorMessage.checkSuccessMessage("Successfully logged in");
    driver.quit();
  }

  @Then("A failed logged in message should appear")
  public void loginErrorResponse() {
    errorMessage.checkSuccessMessage("Login failed");
    driver.quit();
  }
}
