package tqs.exdelivery.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class PlatformDeliveriesPage {
  private final WebDriver driver;

  private final String baseUrl = "http://localhost:3001/";

  public PlatformDeliveriesPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get(baseUrl + "app/adminOrders");
  }

  public void insertCourierEmail(String email) {
    driver.findElement(By.id("courierEmail")).sendKeys(email);
  }

  public void insertOrder(String sortOrder) {
    String sortOption = sortOrder == "ascending" ? "1" : "2";
    {
      WebElement element = driver.findElement(By.id("demo-controlled-open-select"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).clickAndHold().perform();
    }
    {
      WebElement element =
          driver.findElement(By.cssSelector(".MuiButtonBase-root:nth-child(" + sortOption + ")"));
      Actions builder = new Actions(driver);
      builder.moveToElement(element).release().perform();
    }
    driver.findElement(By.cssSelector("body")).click();
    driver.findElement(By.cssSelector(".MuiButtonBase-root:nth-child(" + sortOption + ")")).click();
  }

  public void pressSearchButton() {
    driver.findElement(By.cssSelector(".MuiButton-contained")).click();
  }

  public void checkDeliveriesMoreThanOne() {
    {
      List<WebElement> elements =
          driver.findElements(
              By.cssSelector(".MuiTableRow-hover:nth-child(1) > .MuiTableCell-root:nth-child(1)"));
      assert (elements.size() > 0);
    }
  }

  public void pressFirstOne() {
    driver
        .findElement(
            By.cssSelector(".MuiTableRow-hover:nth-child(1) > .MuiTableCell-root:nth-child(3)"))
        .click();
    driver.quit();
  }
}
