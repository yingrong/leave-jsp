package com.tw.todo_list;

import java.util.*;
import java.util.stream.Collectors;

public class TodoRepository {

    private static Map<String, Todo> todoMap = new HashMap<>();

    {
        Todo todo1 = new Todo("1", "test add new todo", false);
        todoMap.put(todo1.getId(), todo1);
    }

    public void addTodo(Todo todo) {
        todoMap.put(todo.getId(), todo);
    }

    public List<Todo> getTodoList() {
        return todoMap.values().stream().sorted().collect(Collectors.toList());
    }

    public void deleteTodo(String id) {
        if (todoMap.containsKey(id)) {
            todoMap.remove(id);
        }
    }

    public void markDone(String id) {
        if (todoMap.containsKey(id)) {
            todoMap.get(id).setCompleted(true);
        }
    }

    public void markUnfinished(String id) {
        if (todoMap.containsKey(id)) {
            todoMap.get(id).setCompleted(false);
        }
    }

    public void deleteCompleted() {
        List<String> idList = todoMap.values().stream()
                .filter(Todo::getCompleted)
                .map(Todo::getId)
                .collect(Collectors.toList());
        idList.forEach(this::deleteTodo);
    }
}
