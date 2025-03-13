import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.time.Duration;
import java.util.Set;

public class MyntraCartTest {
    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.addArguments("--disable-notifications"); // Disable browser notifications

        driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.myntra.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));
    }

    @Test
    public void addToCartWorkflow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Actions actions = new Actions(driver);
        String parentWindow = driver.getWindowHandle();

        // Navigate to Men -> Formal Shirts
        WebElement menCategory = driver.findElement(By.xpath("//a[@data-group='men']"));
        WebElement menFormalShirtLink = driver.findElement(By.xpath("//a[@href='/men-formal-shirts']"));
        actions.moveToElement(menCategory).perform();
        wait.until(ExpectedConditions.visibilityOf(menFormalShirtLink));
        actions.moveToElement(menFormalShirtLink).click().perform();

        // Select the first formal shirt
        WebElement selectedShirt = driver.findElement(By.xpath("(//a[contains(@href,'shirts/invictus')])[1]//div[@class='product-price']"));
        selectedShirt.click();

        // Switch to new tab where shirt details are displayed
        for (String windowHandle : driver.getWindowHandles()) {
            if (!parentWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        System.out.println("Product page title: " + driver.getTitle());

        // Extract product details
        WebElement shirtBrand = driver.findElement(By.xpath("//h1[@class='pdp-title']"));
        WebElement shirtDescription = driver.findElement(By.xpath("//h1[@class='pdp-name']"));
        WebElement shirtPriceElement = driver.findElement(By.xpath("//span[@class='pdp-price']/strong"));
        WebElement sizeSelection = driver.findElement(By.xpath("//p[text()='42']/ancestor::button"));
        WebElement addToBagButton = driver.findElement(By.xpath("//div[contains(@class,'pdp-add-to-bag')]"));

        int shirtPrice = Integer.parseInt(shirtPriceElement.getText().substring(1));
        int selectedSize = Integer.parseInt(sizeSelection.getText());

        System.out.println("Brand: " + shirtBrand.getText());
        System.out.println("Description: " + shirtDescription.getText());
        System.out.println("Price: " + shirtPrice);
        System.out.println("Selected Size for selected item: " + selectedSize);

        // Add item to cart
        sizeSelection.click();
        addToBagButton.click();

        // Validate cart count
        WebElement cartItemCount = driver.findElement(By.xpath("//span[contains(@class,'desktop-badge')]"));
        wait.until(ExpectedConditions.visibilityOf(cartItemCount));
        int cartItemCountValue = Integer.parseInt(cartItemCount.getText());
        System.out.println("Cart Item Count: " + cartItemCountValue);
        Assert.assertEquals(cartItemCountValue, 1);

        // Open cart page
        WebElement cartIcon = driver.findElement(By.xpath("(//a[@class='desktop-cart'])/span[1]"));
        cartIcon.click();

        // Validate item details in the cart
        WebElement selectedCartItem = driver.findElement(By.xpath("//span[@class='bulkActionStrip-itemSelected']"));
        wait.until(ExpectedConditions.visibilityOf(selectedCartItem));

        WebElement cartSizeElement = driver.findElement(By.cssSelector("div.itemComponents-base-size span"));
        int cartSize = Integer.parseInt(cartSizeElement.getText().substring(6));
        System.out.println("Size in Cart for item: " + cartSize);

        WebElement cartQuantityElement = driver.findElement(By.cssSelector("div.itemComponents-base-quantity span"));
        int cartQuantity = Integer.parseInt(cartQuantityElement.getText().split(": ")[1]);
        System.out.println("Quantity in Cart: " + cartQuantity);

        WebElement cartPriceElement = driver.findElement(By.cssSelector("div.itemComponents-base-price div"));
        int cartPrice = Integer.parseInt(cartPriceElement.getText());
        System.out.println("Price in Cart: " + cartPrice);

        Assert.assertEquals(cartQuantity, cartItemCountValue);
        Assert.assertEquals(cartSize, selectedSize);
        Assert.assertEquals(cartPrice, shirtPrice);

        // Validate total price in checkout
        int platformFee = Integer.parseInt(driver.findElement(By.xpath("//span[text()='Platform Fee']/..//span[@class='priceDetail-base-value']/span")).getText());
        int finalCartValue = Integer.parseInt(driver.findElement(By.xpath("//span[@class='priceDetail-base-redesignRupeeTotalIcon']/..")).getText().substring(1));
        Assert.assertEquals(finalCartValue, (platformFee + cartPrice));
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
