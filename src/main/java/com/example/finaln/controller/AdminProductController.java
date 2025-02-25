package com.example.finaln.controller;


import com.example.finaln.dto.AdminProductDto;
import com.example.finaln.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@Slf4j
public class AdminProductController {


    private final AdminProductService adminProductService; // @RequiredArgsConstructor가 자동 주입



    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("productCategory") String category,
            @RequestParam("productColor") String color,
            @RequestParam("productSize") String size,
            @RequestParam("productPrice") int price,
            @RequestParam("productImage") MultipartFile image
    ) {
        try {
            adminProductService.addProduct(productName, category, color, size, price, image);  // 변수명 수정
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<AdminProductDto>> getProductList() {
        List<AdminProductDto> productList = adminProductService.getAllProducts();
        return ResponseEntity.ok(productList);
    }

    // ✅ 특정 상품 상세 조회 (GET 요청)
    @GetMapping("/{id}")
    public ResponseEntity<AdminProductDto> getProductById(@PathVariable Long id) {
        AdminProductDto product = adminProductService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ✅ 상품 수정 (PUT 요청)
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long id,
            @RequestParam("productName") String productName,
            @RequestParam("productCategory") String category,
            @RequestParam("productColor") String color,
            @RequestParam("productSize") String size,
            @RequestParam("productPrice") int price,
            @RequestParam("productStock") int stock,
            @RequestParam(value = "productImage", required = false) MultipartFile image) {

        try {
            boolean isUpdated = adminProductService.updateProduct(id, productName, category, color, size, price, stock,image);
            if (isUpdated) {
                return ResponseEntity.ok("Product updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product: " + e.getMessage());
        }
    }


    // ✅ 상품 삭제 컨트롤러 (DELETE 요청)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = adminProductService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.ok(Map.of("success", true, "message", "상품이 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "error", "상품을 찾을 수 없습니다."));
        }
    }

}
