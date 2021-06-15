package tqs.exdelivery.frontend;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.platform.engine.Cucumber;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tqs.exdelivery.ExDeliveryApplication;

@Cucumber
@CucumberOptions(plugin = {"pretty"})
@CucumberContextConfiguration
@SpringBootTest(
    classes = ExDeliveryApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"security.basic.enabled=false"})
@AutoConfigureTestDatabase
public class CucumberTestRunner {}
