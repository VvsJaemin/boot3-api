package org.jm.apiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jm.apiserver.dto.PageRequestDTO;
import org.jm.apiserver.dto.PageResponseDTO;
import org.jm.apiserver.dto.ProductDto;
import org.jm.apiserver.service.ProductService;
import org.jm.apiserver.util.CustomFileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CustomFileUtil fileUtil;

    @PostMapping("/")
    public Map<String, Object> register(ProductDto productDto) {

        log.info("register = {}" + productDto);

        List<MultipartFile> files = productDto.getFiles();

        List<String> uploadFilenames = fileUtil.saveFiles(files);

        productDto.setUploadFileNames(uploadFilenames);

        Long pno = productService.register(productDto);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("RESULT", pno);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName) {
        return fileUtil.getFile(fileName);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDto> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDto read(@PathVariable(name = "pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable(name = "pno") Long pno, ProductDto productDto) {
        productDto.setPno(pno);

        ProductDto oldProductDto = productService.get(pno);

        // 기존 파일들(db 존재)
        List<String> oldFileNames = oldProductDto.getUploadFileNames();

        // 새로 업로드 해야하는 파일들

        List<MultipartFile> files = productDto.getFiles();

        // 새로 업로드 되어서 생성된 파일
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // 화면에서 변화 없이 계속 유지되는 파일
        List<String> uploadFileNames = productDto.getUploadFileNames();

        // 유지되는 파일 + 새로 업로드 파일 저장해야하는 파일 목록

        if (currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDto);

        if (oldFileNames != null && oldFileNames.size() > 0) {
            // 지워야하는 파일 목록 찾기
            // 예전 파일들 중에서 지워져야 하는 파일이름들

            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadFileNames.indexOf(fileName) == -1).collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");

    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {
        // 삭제해야할 파일들 알아 내기
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");
    }
}
