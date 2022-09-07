package com.tw.e2e.page_object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    private final By todoListJspBy = By.id("jsp-page");
    private WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("http://localhost:8080/");
    }

    public TodoListPage gotoTodoListPage() {
        driver.findElement(todoListJspBy).click();
        return new TodoListPage(driver);
    }
}
