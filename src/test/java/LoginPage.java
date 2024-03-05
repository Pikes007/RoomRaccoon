import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginPage extends BaseTestClass {
	
	private static final By loginButton = By.xpath("//button[@id='login']");
    private static final By usernameField = By.xpath("//input[@id='userName']");
    private static final By passwordField = By.xpath("//input[@id='password']");
    private static final By newUserButton = By.xpath("//button[@id='newUser']");
    private static final By registerButton = By.xpath("//button[@id='register']");
    private static final By registerFirstName = By.xpath("//input[@id='firstname']");
    private static final By registerLastName = By.xpath("//input[@id='lastname']");
    private static final By captchaMessage = By.xpath("//div[@class='col-md-12 col-sm-12']");
    private static final By bookCollection = By.cssSelector(".action-buttons");

    /**
     * Returns the text of the captcha message.
     *
     * @return Captcha message text
     */
    public String returnCaptchaMessage() {
        String captchaVerifyMessage = driver.findElement(captchaMessage).getText();
        return captchaVerifyMessage;
    }

    /**
     * Creates a new account by filling in the registration form.
     *
     * @param username User's username
     * @param password User's password
     */
    public void createAccount(String username, String password) {
        driver.findElement(loginButton).click();
        WebElement elementToScroll = driver.findElement(newUserButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToScroll);
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        elementToScroll.click();
        driver.findElement(registerFirstName).sendKeys("TestFirstName");
        driver.findElement(registerLastName).sendKeys("TestLastName");
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        driver.findElement(registerButton).click();
    }

    /**
     * Retrieves the list of book names displayed on the page.
     *
     * @return List of book names as strings
     */
    public List<String> getBookList() {
        List<WebElement> booksList = driver.findElements(bookCollection);
        List<String> booksListText = new ArrayList<>();

        for (WebElement book : booksList) {
            booksListText.add(book.getText() + "\n");
        }
        return booksListText;
    }

    /**
     * Selects an item from the provided collection by clicking on it.
     *
     * @param collection List of items to choose from
     * @param item       Item to be selected
     */
    public void selectItemFromList(List<String> collection, String item) {
        System.out.println(collection);
        System.out.println(item);
        for (String listItem : collection) {
            System.out.println("Comparing: " + listItem + " with " + item);
            if (listItem.trim().equals(item.trim())) {
                System.out.println(item + " was found");
                String xpath = String.format("//a[normalize-space()='%s']", item);
                System.out.println(xpath);
                WebElement itemElement = driver.findElement(By.xpath(xpath));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", itemElement);
                itemElement.click();
                System.out.println("Clicked on the element: " + itemElement);
                return;
            }
        }
    }

    /**
     * Test attempts to complete the registration form to register a new member,
     * then attempts to submit the registration form without ticking captcha verification,
     * and verifies the presence of the captcha error message.
     */
    @Test
    public void testRegistrationWithMissingCaptcha() {
        createAccount("yourUserName", "yourPassword");
        Assert.assertTrue(returnCaptchaMessage().contains("Please verify reCaptcha to register!"));
    }

    /**
     * Test gets all books displayed into a list,
     * gets input from the test, and selects the item from the list to click on.
     * Verifies that the user lands on the correct page by checking the URL.
     */
    @Test
    public void testSelectingBook() {
        List<String> allBooksDisplayed = getBookList();
        selectItemFromList(allBooksDisplayed, "Speaking JavaScript");
        String currentUrl = driver.getCurrentUrl();
        assert currentUrl.equals("https://demoqa.com/books?book=9781449365035") : "Page is not blank";
    }
}
