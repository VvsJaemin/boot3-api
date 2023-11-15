package org.jm.apiserver.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.jm.apiserver.domain.QTodo;
import org.jm.apiserver.domain.Todo;
import org.jm.apiserver.dto.PageRequestDTO;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {


    public TodoSearchImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> search1(PageRequestDTO pageRequestDTO) {

        log.info("search1........");

        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = from(todo);

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                            pageRequestDTO.getSize(),
                            Sort.by("tno").descending());

        this.getQuerydsl().applyPagination(pageable, query); // 페이징 처리와, 쿼리 조건을 포함

        List<Todo> list = query.fetch(); // 목록 데이터 반환

        long total = query.fetchCount();// 카운트

        return new PageImpl<>(list, pageable, total);
    }
}
