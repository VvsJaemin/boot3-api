package org.jm.apiserver.repository.search;

import org.jm.apiserver.domain.Todo;
import org.springframework.data.domain.Page;

public interface TodoSearch {

    Page<Todo> search1();
}
