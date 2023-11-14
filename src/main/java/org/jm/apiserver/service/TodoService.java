package org.jm.apiserver.service;

import org.jm.apiserver.domain.Todo;
import org.jm.apiserver.dto.TodoDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TodoService {

    TodoDTO get(Long tno);

    default TodoDTO entityToDTO(Todo todo) {

        TodoDTO todoDTO =
                TodoDTO.builder()
                        .tno(todo.getTno())
                        .title(todo.getTitle())
                        .content(todo.getContent())
                        .complete(todo.isComplete())
                        .dueDate(todo.getDueDate())
                        .build();

        return todoDTO;
    }

    default Todo dtoToEntity(TodoDTO todo) {

        return Todo.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .complete(todo.isComplete())
                .dueDate(todo.getDueDate())
                .build();

    }
}