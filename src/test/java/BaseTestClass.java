import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTestClass {

    protected WebDriver driver;
    protected String baseUrl = "https://demoqa.com/books";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();  
        driver.manage().window().maximize();
        driver.get(baseUrl);
    }
/*
    @AfterMethod
    public void tearDown() {
        // Close the WebDriver instance
        if (driver != null) {
            driver.quit();
        }
    }*/
}
