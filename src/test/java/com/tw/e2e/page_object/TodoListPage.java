package com.tw.e2e.page_object;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class TodoListPage {

    private WebDriver driver;

    private By todoInputBy = By.className("new-todo");

    public TodoListPage(WebDriver driver) {
        this.driver = driver;
    }

    public TodoListPage addTodo(String todo) {
        WebElement todoInput = driver.findElement(todoInputBy);
        todoInput.sendKeys(todo + Keys.ENTER);

        return this;
    }

    public boolean todoListContains(String todo) {
        WebElement ul = driver.findElement(By.className("todo-list"));
        List<WebElement> list = ul.findElements(By.tagName("li"));
        List<String> todoList = list.stream()
                .map(li -> {
                    WebElement label = li.findElement(By.tagName("label"));
                    return label;
                })
                .map(webElement -> webElement.getText())
                .collect(Collectors.toList());

        return todoList.contains(todo);
    }
}

