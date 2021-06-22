package tqs.exdelivery.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DeliveryDetailsPage {
    private final WebDriver driver;

    private final String baseUrl = "http://localhost:3001/";

    public DeliveryDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void goTo() {
        driver.get(baseUrl + "app/delivery/1");
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
