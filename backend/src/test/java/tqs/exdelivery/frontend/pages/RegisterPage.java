package tqs.exdelivery.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {
  private final WebDriver driver;
  private final String baseUrl = "http://localhost:3001/";

  public RegisterPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get(baseUrl + "register");
  }

  public void insertEmail(String email) {
    driver.findElement(By.name("email")).sendKeys(email);
  }

  public void insertName(String name) {
    driver.findElement(By.name("name")).sendKeys(name);
  }

  public void insertPassword(String password) {
    driver.findElement(By.name("password")).sendKeys(password);
  }

  public void pressRegister() {
    driver.findElement(By.cssSelector(".MuiButton-label")).click();
  }
}
