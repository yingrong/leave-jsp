package com.tw.e2e;

import com.tw.e2e.page_object.HomePage;
import com.tw.e2e.page_object.TodoListPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;

public class TodoListTest {

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
    public void should_add_new_todo() {
        // 打开首页，点击进入totoList demo ：jsp版本
        HomePage homePage = new HomePage(driver);
        TodoListPage todoListPage = homePage.open()
                .gotoTodoListPage();

        // 新增一个 todo项后，list会增加
        assertTrue(todoListPage.addTodo("todo1").todoListContains("todo1"));
    }

    @After
    public void tearDown() {
        if (driver == null) {
            driver.quit();
        }
    }
}
