package org.jm.apiserver.repository;

import org.jm.apiserver.domain.Todo;
import org.jm.apiserver.repository.search.TodoSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
