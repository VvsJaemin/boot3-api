package org.jm.apiserver.service;

import org.jm.apiserver.dto.PageRequestDTO;
import org.jm.apiserver.dto.PageResponseDTO;
import org.jm.apiserver.dto.ProductDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDto> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDto productDto);

    ProductDto get(Long pno);

    void modify(ProductDto productDto);

    void remove(Long pno);


}
