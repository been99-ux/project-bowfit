// main.html 에 대한 탭 기능 메서드 ↓
$(document).ready(function () {
    $('.tab-button').on('click', function () {
        // 모든 버튼과 콘텐츠 초기화
        $('.tab-button').removeClass('active');
        $('.tab-content').removeClass('active');

        // 클릭된 버튼 활성화 및 해당 콘텐츠 표시
        $(this).addClass('active');
        const target = $(this).data('tab');
        $('#' + target).addClass('active');
    });
});

// CART LIST 장바구니 갯수 추가 메서드 !↓
$(document).ready(function () {
    const $minusButton = $(".minus");
    const $plusButton = $(".plus");
    const $inputBox = $(".input-box");
    const $priceCell = $(".price");
    const $totalProductPrice = $(".total-product-price");
    const $finalPrice = $(".final-price");
    const $rewardPoints = $(".reward-points");

    // 초기 단가 가져오기
    const unitPrice = parseInt($priceCell.data("unit-price"));
    const shippingCost = 0; // 배송비 (필요시 변경 가능)

    // 업데이트 함수
    function updatePrice(quantity) {
        const totalPrice = unitPrice * quantity; // 상품 금액
        const finalPrice = totalPrice + shippingCost; // 결제 예정 금액
        const rewardPoints = (totalPrice * 0.05).toFixed(1); // 적립 예정 금액 (1%)

        // 상품 금액 업데이트
        $priceCell.text(totalPrice.toLocaleString() + "원");
        $totalProductPrice.text(totalPrice.toLocaleString() + "원");

        // 결제 예정 금액 업데이트
        $finalPrice.text("=" + finalPrice.toLocaleString() + "원");

        // 적립 예정 금액 업데이트
        $rewardPoints.text("최대 " + rewardPoints + "원");
    }

    // + 버튼 클릭
    $plusButton.click(function () {
        let quantity = parseInt($inputBox.val());
        if (quantity < parseInt($inputBox.attr("max"))) {
            quantity++;
            $inputBox.val(quantity);
            updatePrice(quantity);
        }
    });

    // - 버튼 클릭
    $minusButton.click(function () {
        let quantity = parseInt($inputBox.val());
        if (quantity > parseInt($inputBox.attr("min"))) {
            quantity--;
            $inputBox.val(quantity);
            updatePrice(quantity);
        }
    });
});



// CART LIST 쇼핑계속하기 버튼에 대한 뒤로가기 메서드 ↓
$(document).ready(function () {
    $("#continue-shopping").click(function (event) {
        event.preventDefault(); // 기본 동작 막기
        history.back(); // 이전 페이지로 이동
    });
});

// WISH LIST 삭제하기 버튼
$(document).ready(function () {
    // 개별 삭제 버튼 기능
    $(".delete-item").click(function (e) {
        e.preventDefault();  // 링크 기본 동작 방지
        $(this).closest("tr").remove();  // 해당 상품 행 삭제
    });

    // 선택 삭제 기능
    $("#delete-selected").click(function (e) {
        e.preventDefault();
        $(".cart-checkbox:checked").closest("tr").remove();
    });
});

// community 캐러셀 메서드
jQuery(document).ready(function($) {
    "use strict";
    $('#customers-testimonials')( {
        loop: true,
        center: true,
        items: 3,
        margin: 30,
        autoplay: true,
        dots:true,
        nav:true,
        autoplayTimeout: 8500,
        smartSpeed: 450,
        navText: ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],
        responsive: {
            0: {
                items: 1
            },
            768: {
                items: 2
            },
            1170: {
                items: 3
            }
        }
    });
});