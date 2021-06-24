package tqs.exdelivery.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CourierPage {
  private final WebDriver driver;
  private final String baseUrl = "http://localhost:3001/";

  public CourierPage(WebDriver driver) {
    this.driver = driver;
  }

  public void goTo() {
    driver.get(baseUrl + "app/couriers");
  }

  public int getNumCouriers() {
    List<WebElement> rows = driver.findElements(By.tagName("tr"));
    return rows.size() - 1;
  }

  public String getCourierName(int courierId) {
    List<WebElement> rows = driver.findElements(By.tagName("tr"));

    for (WebElement row : rows) {
      List<WebElement> cols = row.findElements(By.tagName("td"));
      if (cols.size() > 0 && cols.get(0).getText().equals(String.valueOf(courierId))) {
        return cols.get(1).getText();
      }
    }
    return null;
  }

  public void fireCourier(int courierId) {
    List<WebElement> rows = driver.findElements(By.tagName("tr"));

    for (WebElement row : rows) {
      List<WebElement> cols = row.findElements(By.tagName("td"));
      if (cols.size() >= 5 && cols.get(0).getText().equals(String.valueOf(courierId))) {
        cols.get(5).click();
      }
    }
  }
}
