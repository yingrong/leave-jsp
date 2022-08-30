package com.tw.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestDemo {

    WebDriver driver;

    @BeforeClass
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setUpTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }


    @Test
    @Ignore
    public void should_use_chrome() {
        System.setProperty("webdriver.chrome.driver","/Users/somebody/bin/chromedriver");
        ChromeDriver driver = new ChromeDriver();

        driver.get("https://www.baidu.com/");

        driver.quit();
    }

    @Test
    public void should_test_user_web_driver_manager() {
        driver.get("https://www.baidu.com/");

        driver.quit();
    }
}
