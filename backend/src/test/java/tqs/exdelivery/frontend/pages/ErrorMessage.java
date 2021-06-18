package tqs.exdelivery.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ErrorMessage {

  private final WebDriver driver;

  public ErrorMessage(WebDriver driver) {
    this.driver = driver;
  }

  public void checkErrorMessage(String message) {
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".Toastify__toast-body")));
    assertThat(driver.findElement(By.cssSelector(".Toastify__toast-body")).getText(), is(message));
  }

  public void checkSuccessMessage(String message) {
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".Toastify__toast-body")));
    assertThat(driver.findElement(By.cssSelector(".Toastify__toast-body")).getText(), is(message));
  }
}
