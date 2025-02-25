$(document).ready(function () {
    loadProductList(); // ✅ 페이지 로드 시 상품 목록 불러오기

    // 🔹 상품 추가 버튼 이벤트
    $("#addProductBtn").click(addProduct);

    // 🔹 상품 수정 버튼 이벤트
    $("#updateProductBtn").click(updateProduct);

    // 🔹 상품 삭제 버튼 이벤트
    $("#deleteProductBtn").click(deleteProduct);

    // 🔹 상품 클릭 이벤트 (이벤트 위임 방식)
    $("#productList").on("click", ".products-row", function () {
        let productId = $(this).data("id");
        openEditModal(productId);
    });
});

// ✅ 상품 리스트 불러오기
function loadProductList() {
    $.ajax({
        url: "/admin/product/list",
        type: "GET",
        dataType: "json",
        success: function (data) {
            let productHtml = "";
            data.forEach(product => {
                productHtml += `
                    <div class="products-row" data-id="${product.id}">
                        <div class="product-cell image">
                            <img src="${product.imageUrl ? product.imageUrl : '/images/no-image.png'}" alt="product">
                            <span>${product.name}</span>
                        </div>
                        <div class="product-cell category">${product.category}</div>
                        <div class="product-cell sales">${product.sales}</div>
                        <div class="product-cell stock">${product.stock}</div>
                        <div class="product-cell price">${product.price}원</div>
                    </div>
                `;
            });
            $("#productList").html(productHtml);
        },
        error: function () {
            console.error("🚨 상품 목록을 불러오는 데 실패했습니다.");
        }
    });
}

// ✅ 상품 추가 (AJAX)
function addProduct() {
    let formData = new FormData();
    formData.append("productName", $("#productName").val());
    formData.append("productCategory", $("#productCategory").val());
    formData.append("productColor", $("#productColor").val());
    formData.append("productSize", $("#productSize").val());
    formData.append("productPrice", $("#productPrice").val());
    formData.append("productImage", $("#productImage")[0].files[0]); // 파일 추가

    $.ajax({
        url: "/admin/product/add",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            alert("✅ 상품이 추가되었습니다.");
            loadProductList(); // 리스트 갱신
        },
        error: function (xhr) {
            alert("🚨 상품 추가 실패: " + xhr.responseText);
        }
    });
}

// ✅ 상품 상세 모달 열기
function openEditModal(productId) {
    console.log("🔹 상품 클릭됨! ID:", productId);

    $.ajax({
        url: `/admin/product/${productId}`,
        type: "GET",
        success: function (product) {
            $("#editProductId").val(product.id);
            $("#editProductName").val(product.name);
            $("#editProductCategory").val(product.category);
            $("#editProductColor").val(product.color);
            $("#editProductSize").val(product.size);
            $("#editProductPrice").val(product.price);
            $("#editProductStock").val(product.stock);
            $("#editProductImagePreview").attr("src", product.imageUrl ? product.imageUrl : "/images/no-image.png");

            $("#editProductModal").modal("show"); // ✅ 모달 열기
        },
        error: function () {
            alert("🚨 상품 정보를 불러오는 데 실패했습니다.");
        }
    });
}

// ✅ 상품 수정 (AJAX)
function updateProduct() {
    let formData = new FormData();
    let productId = $("#editProductId").val();
    let imageFile = $("#editProductImage")[0].files[0];

    formData.append("productName", $("#editProductName").val());
    formData.append("productCategory", $("#editProductCategory").val());
    formData.append("productColor", $("#editProductColor").val());
    formData.append("productSize", $("#editProductSize").val());
    formData.append("productPrice", $("#editProductPrice").val());
    formData.append("productStock", $("#editProductStock").val());

    if (imageFile) {
        formData.append("productImage", imageFile);
    }

    $.ajax({
        url: `/admin/product/update/${productId}`,
        type: "PUT",
        data: formData,
        processData: false,
        contentType: false,
        success: function () {
            alert("✅ 상품이 수정되었습니다.");
            $("#editProductModal").modal("hide");
            loadProductList(); // 리스트 갱신
        },
        error: function (xhr) {
            alert("🚨 상품 수정 실패: " + xhr.responseText);
        },
    });
}

// ✅ 상품 삭제 (AJAX)
function deleteProduct() {
    let productId = $("#editProductId").val();
    if (!productId) {
        alert("🚨 삭제할 상품이 선택되지 않았습니다.");
        return;
    }

    if (!confirm("정말로 삭제하시겠습니까?")) return;

    $.ajax({
        url: `/admin/product/delete/${productId}`,
        type: "DELETE",
        success: function (response) {
            alert("🗑 상품이 삭제되었습니다.");
            $("#editProductModal").modal("hide");
            loadProductList(); // 리스트 갱신
        },
        error: function (xhr) {
            alert("🚨 상품 삭제 중 오류 발생: " + xhr.responseText);
        },
    });
}
