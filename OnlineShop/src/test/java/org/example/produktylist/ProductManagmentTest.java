package org.example.produktylist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManagmentTest {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;
        vars = new HashMap<>();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:8080/login");
        driver.manage().window().setSize(new Dimension(1200, 907));
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.cssSelector(".btn")).click();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testPrzegladanieProduktu() {
        driver.findElement(By.xpath("//a[contains(@href, '/product/1/details')]")).click();
    }


    @Test
    public void testDodawanieProduktu() {
        driver.findElement(By.cssSelector("a.add-icon")).click();  // kliknij "+" (dodaj produkt)

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));

        driver.findElement(By.id("name")).sendKeys("Test Produkt");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("price")).sendKeys("25.99");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement categoryDropdown = driver.findElement(By.id("category"));
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        categoryDropdown.click();
        WebElement categoryOption = driver.findElement(By.xpath("//option[text()='RTV']"));
        categoryOption.click();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.id("description1")).sendKeys("Opis produktu - część 1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElement(By.id("description2")).sendKeys("Opis produktu - część 2");

        driver.findElement(By.cssSelector(".save-button")).click();

        wait.until(ExpectedConditions.urlContains("/product"));

        assertTrue(driver.getCurrentUrl().contains("/product"));
    }




    @Test
    public void testEdytowanieProduktu() {
        WebElement lastProductCard = driver.findElement(By.cssSelector(".products-grid .product-card:last-child"));

        lastProductCard.findElement(By.cssSelector(".product-image")).click();

        driver.findElement(By.linkText("✏️")).click();

        WebElement nameField = driver.findElement(By.id("name"));
        nameField.clear();
        nameField.sendKeys("testNazwaPoEdycji");

        driver.findElement(By.cssSelector(".save-button")).click();
    }


    @Test
    public void testUsuwanieOstatniegoProduktu() {
        WebElement lastProduct = driver.findElement(By.cssSelector(".products-grid .product-card:last-child"));

        lastProduct.findElement(By.cssSelector(".product-image")).click();

        driver.findElement(By.linkText("❌")).click();

        assertEquals("Czy na pewno chcesz usunąć ten produkt?", driver.switchTo().alert().getText());

        driver.switchTo().alert().accept();

        driver.findElement(By.cssSelector("a[href='/product/']")).click();

        List<WebElement> products = driver.findElements(By.cssSelector(".products-grid .product-card"));
    }

}
