package com.tw.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

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
        System.setProperty("webdriver.chrome.driver", "/Users/somebody/bin/chromedriver");
        ChromeDriver driver = new ChromeDriver();

        driver.get("https://www.baidu.com/");
    }

    @Test
    public void should_test_user_web_driver_manager() {
        // 打开首页，点击进入totoList demo ：jsp版本
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("jsp-page")).click();

        // 新增一个 todo 项
        WebElement element = driver.findElement(By.className("new-todo"));
        element.sendKeys("todo1" + Keys.ENTER);

        try {
            System.out.println(new Date());
            Thread.sleep(1000);
            System.out.println(new Date());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 查看todo list
        WebElement ul = driver.findElement(By.className("todo-list"));
        List<WebElement> list = ul.findElements(By.tagName("li"));
        List<String> todoList = list.stream()
                .map(li -> {
                    WebElement label = li.findElement(By.tagName("label"));
                    return label;
                })
                .map(webElement -> webElement.getText())
                .collect(Collectors.toList());


        // 验证 todoList 中有新增的 待办事项
        assertThat(todoList, hasItem("todo1"));
    }

    @After
    public void tearDown() {
        if (driver == null) {
            driver.quit();
        }
    }
}
