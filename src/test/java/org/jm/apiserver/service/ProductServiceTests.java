package org.jm.apiserver.service;

import lombok.extern.log4j.Log4j2;
import org.jm.apiserver.dto.PageRequestDTO;
import org.jm.apiserver.dto.PageResponseDTO;
import org.jm.apiserver.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    ProductService productService;

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDto> result = productService.getList(pageRequestDTO);

        result.getDtoList().forEach(dto -> log.info(dto));
    }

    @Test
    public void testRegister() {
        ProductDto productDto = ProductDto.builder()
                .pname("새로운 상품")
                .pdesc("신규 추가 상품입니다.")
                .price(1000)
                .build();

        productDto.setUploadFileNames(List.of(UUID.randomUUID()+"_" +"Test1.jpg",UUID.randomUUID()+"_" +"Test2.jpg"));

        productService.register(productDto);
    }

    @Test
    public void testRead() {
        Long pno = 12L;

        ProductDto productDto = productService.get(pno);

        log.info(productDto);
        log.info(productDto.getUploadFileNames());
    }
}
