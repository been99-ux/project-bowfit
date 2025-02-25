$(document).ready(function () {
    // 초기 로딩 시 "티셔츠/원피스" 탭 버튼 활성화
    $(".tab-button[data-tab='티셔츠/원피스']").addClass("active");

    // 초기 로딩 시 "티셔츠/원피스" 카테고리 상품을 불러옴
    loadProducts("티셔츠/원피스");

    $(".tab-button").click(function () {
        $(".tab-button").removeClass("active");
        $(this).addClass("active");

        let category = $(this).data("tab");
        loadProducts(category);
    });
});

function loadProducts(category = null) {
    let url = "/api/products";
    if (category) {
        url += "?category=" + encodeURIComponent(category); // 한글 카테고리 값 인코딩
    }

    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (products) {
            updateProductList(products, category);
        },
        error: function (xhr, status, error) {
            console.error("상품 목록을 불러오는 데 실패했습니다.", error);
        }
    });
}

function updateProductList(products, category) {
    // data-tab 속성을 기반으로 컨테이너 선택
    let container = $(`.tab-content[data-tab="${category}"] .item_box`);

    if (!container.length) {
        console.warn("카테고리 영역을 찾을 수 없음:", category);
        return;
    }

    let productHtml = "";
    if (products && products.length > 0) {
        products.forEach(product => {
            productHtml += `
              <a href="/productinfo/${product.id}" class="item_content">
                  <div>
                      <img class="item_image" src="${product.imageUrl}" alt="${product.name}">
                  </div>
                  <div>
                      <div>
                          <span>${product.name}</span><br>
                          <span>${product.color || '색상 없음'} / ${product.size || '사이즈 없음'}</span>
                      </div>
                      <div class="item_info">
                          <p class="price">${product.price ? product.price.toLocaleString() + '원' : '가격 정보 없음'}</p>
                      </div>
                  </div>
              </a>
          `;
        });
    } else {
        productHtml = "<p>해당 카테고리에 상품이 없습니다.</p>";
    }

    container.html(productHtml);
}