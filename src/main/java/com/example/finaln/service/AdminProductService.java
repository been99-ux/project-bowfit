package com.example.finaln.service;

import com.example.finaln.dto.AdminProductDto;
import com.example.finaln.entity.Product;
import com.example.finaln.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;





@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductService {

    private final AdminProductRepository productRepository;  // final 필드 사용
    private final String uploadDir = "C:/upload/"; // 이미지 저장 경로


    public void addProduct(String productName, String category, String color, String size, int price, MultipartFile image) throws IOException {
        String imagePath = saveImage(image);

        Product product = Product.builder()
                .name(productName)
                .category(category)
                .color(color)
                .size(size)
                .price(price)
                .imageUrl(imagePath)
                .sales(0)
                .build();

        productRepository.save(product);
    }

    private String saveImage(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {  // null 체크 추가
            return null;
        }
        String uploadDir = "C:/upload/";
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.write(path, image.getBytes());

        return "/upload/" + fileName;  // 웹에서 접근 가능하도록 URL 경로 반환
    }

    public List<AdminProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> new AdminProductDto(
                        product.getId(),
                        product.getCategory(),
                        product.getImageUrl(),
                        product.getName(),
                        product.getPrice(),
                        product.getSales(),
                        product.getStock(),
                        product.getColor(),
                        product.getSize()
                ))
                .collect(Collectors.toList());
    }

    // ✅ 특정 상품 상세 조회
    public AdminProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> new AdminProductDto(
                        product.getId(),
                        product.getCategory(),
                        product.getImageUrl(),
                        product.getName(),
                        product.getPrice(),
                        product.getSales(),
                        product.getStock(),
                        product.getColor(),
                        product.getSize()
                ))
                .orElse(null);
    }

    // ✅ 상품 수정 (이미지도 수정 가능)
    public boolean updateProduct(Long id, String productName, String category, String color, String size, int price, int stock ,MultipartFile image) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productName);
            product.setCategory(category);
            product.setColor(color);
            product.setSize(size);
            product.setStock(stock);
            product.setPrice(price);

            // ✅ 이미지 변경 시 기존 이미지 삭제 후 새 이미지 저장
            if (image != null && !image.isEmpty()) {
                deleteImageFile(product.getImageUrl()); // 기존 이미지 삭제
                String imagePath = saveImage(image);
                product.setImageUrl(imagePath);
            }

            productRepository.save(product);
            return true;
        }
        return false;
    }

    // ✅ 상품 삭제 (DB + 로컬 파일)
    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // ✅ 이미지 파일도 삭제
            deleteImageFile(product.getImageUrl());

            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ 이미지 파일 삭제 메서드
    private void deleteImageFile(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String filePath = imageUrl.replace("/upload/", "C:/upload/");
            try {
                Files.deleteIfExists(Paths.get(filePath)); // 이미지 파일 삭제
                log.info("🗑 이미지 삭제됨: " + filePath);
            } catch (IOException e) {
                log.error("🚨 이미지 삭제 실패: " + filePath, e);
            }
        }
    }
}