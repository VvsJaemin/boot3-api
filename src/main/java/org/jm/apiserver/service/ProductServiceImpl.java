package org.jm.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.jm.apiserver.domain.Product;
import org.jm.apiserver.domain.ProductImage;
import org.jm.apiserver.dto.PageRequestDTO;
import org.jm.apiserver.dto.PageResponseDTO;
import org.jm.apiserver.dto.ProductDto;
import org.jm.apiserver.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public PageResponseDTO<ProductDto> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );
        Page<Object[]> result = productRepository.selectList(pageable);

        List<ProductDto> dtoList = result.get().map(arr -> {
            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            ProductDto productDto = ProductDto.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .pdesc(product.getPdesc())
                    .price(product.getPrice())
                    .build();

            String imageStr = productImage.getFileName();
            productDto.setUploadFileNames(List.of(imageStr));

            return productDto;

        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDto>withAll()
                .dtoList(dtoList)
                .total(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();


    }

    @Override
    public Long register(ProductDto productDto) {
        Product product = dtoToEntity(productDto);

        Product result = productRepository.save(product);

        return result.getPno();
    }

    @Override
    public ProductDto get(Long pno) {
        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        ProductDto productDto = entityToDTO(product);

        return productDto;
    }

    @Override
    public void modify(ProductDto productDto) {
        Optional<Product> result = productRepository.findById(productDto.getPno());

        Product product = result.orElseThrow();

        product.changeName(product.getPname());
        product.changeDesc(product.getPdesc());
        product.changePrice(product.getPrice());


        product.clearList(); // 기존 파일 삭제 후 새로 업로드

        List<String> uploadFileNames = productDto.getUploadFileNames();

        if (uploadFileNames != null && uploadFileNames.size() > 0) {
            uploadFileNames.stream().forEach(uploadName ->{
                product.addImageString(uploadName);
            });
        }

        productRepository.save(product);


    }

    @Override
    public void remove(Long pno) {
        productRepository.updateToDelete(pno, true);
    }

    private Product dtoToEntity(ProductDto productDto) {
        Product product = Product.builder()
                .pno(productDto.getPno())
                .pname(productDto.getPname())
                .pdesc(productDto.getPdesc())
                .price(productDto.getPrice())
                .build();

        // 업로드 처리가 끝난 파일들의 이름 리스트

        List<String> uploadFileNames = productDto.getUploadFileNames();

        if (uploadFileNames == null) {
            return product;
        }

        uploadFileNames.stream().forEach(uploadFileName->{
            product.addImageString(uploadFileName);
        });

        return product;
    }

    private ProductDto entityToDTO(Product product) {
        ProductDto productDto = ProductDto.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.size() == 0) {
            return productDto;
        }

        List<String> fileNameList = imageList.stream().map(productImage ->
            productImage.getFileName()).toList();

        productDto.setUploadFileNames(fileNameList);

        return productDto;
    }
}
