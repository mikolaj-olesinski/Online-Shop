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
import java.util.*;

public class OrderManagementTests {
    private WebDriver driver;
    private Map<String, Object> vars;
    private JavascriptExecutor js;
    private WebDriverWait wait;
    private String rootWindow;

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

        driver.findElement(By.cssSelector(".nav-links > a:nth-child(3)")).click();
        rootWindow = driver.getWindowHandle();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testViewOrder() {
        driver.findElement(By.cssSelector(".order-row:nth-child(1) li")).click();

        wait.until(ExpectedConditions.urlContains("/orders/"));

        driver.findElement(By.linkText("Powrót do listy")).click();
        wait.until(ExpectedConditions.urlContains("/orders"));
    }

    @Test
    public void testEditOrder() {
        driver.findElement(By.cssSelector(".order-row:nth-child(1) li")).click();
        wait.until(ExpectedConditions.urlContains("/orders/"));

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Edytuj zamówienie"))).click();

        WebElement lastNameField = wait.until(ExpectedConditions.elementToBeClickable(By.name("dataForm.lastName")));
        lastNameField.clear();
        lastNameField.sendKeys("Nowak");

        driver.findElement(By.cssSelector(".save-button")).click();

        wait.until(ExpectedConditions.urlContains("/orders/"));
    }

    @Test
    public void testCancelEdit() {
        driver.findElement(By.cssSelector(".order-row:nth-child(1) li")).click();
        wait.until(ExpectedConditions.urlContains("/orders/"));

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Edytuj zamówienie"))).click();

        WebElement companyField = wait.until(ExpectedConditions.elementToBeClickable(By.name("dataForm.company")));
        companyField.clear();
        companyField.sendKeys("TEST");

        driver.findElement(By.linkText("Anuluj")).click();

        wait.until(ExpectedConditions.urlContains("/orders/"));
    }

    @Test
    public void testGenerateShippingLabel() {
        driver.findElement(By.cssSelector(".order-row:nth-child(1) li")).click();
        wait.until(ExpectedConditions.urlContains("/orders/"));

        WebElement generateButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Generuj list przewozowy")));
        generateButton.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(driver.getCurrentUrl().contains("/orders/"),
                "Should remain on order page after generating shipping label");
    }

    @Test
    public void testChangeOrderStatus() {
        driver.findElement(By.cssSelector(".order-row:nth-child(1) li")).click();
        wait.until(ExpectedConditions.urlContains("/orders/"));

        WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.name("status")));
        statusDropdown.click();

        WebElement cancelledOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//option[. = 'anulowane']")));
        cancelledOption.click();

        driver.findElement(By.linkText("Powrót do listy")).click();
        wait.until(ExpectedConditions.urlContains("/orders"));
    }
}