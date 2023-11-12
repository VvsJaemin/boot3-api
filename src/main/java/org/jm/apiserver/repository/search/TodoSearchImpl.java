package org.jm.apiserver.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.jm.apiserver.domain.QTodo;
import org.jm.apiserver.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Slf4j
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {


    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1() {

        log.info("search1........");

        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);

        query.where(todo.title.contains("1"));

        query.fetch();

        return null;
    }
}
