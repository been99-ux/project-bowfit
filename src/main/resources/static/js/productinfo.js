// document.querySelectorAll('nav a').forEach(tab => {
//     tab.addEventListener('click', function(e) {
//         e.preventDefault();
//
//         // 모든 탭에서 active 클래스 제거
//         document.querySelectorAll('nav a')
//             .forEach(link =>
//                 link.classList.remove('active'));
//
//         // 클릭된 탭에 active 클래스 추가
//         this.classList.add('active');
//     });
// });

// CART LIST 쇼핑계속하기 버튼에 대한 뒤로가기 메서드 ↓ 아이디 : id="continue__shop"
$(document).ready(function () {
    $("#continue__shop").click(function (event) {
        event.preventDefault(); // 기본 동작 막기
        history.back(); // 이전 페이지로 이동
    });
});

document.addEventListener('DOMContentLoaded', function() {
    // 예시 데이터: 문의게시글 리스트 (실제에서는 서버에서 받아올 데이터)
    const qnaPosts = [
    ];

    // 문의게시글을 표시하는 컨테이너
    const qnaContainer = document.getElementById('qnaContainer');
    const noQnaImage = document.getElementById('noQnaImage');

    // 최대 4개의 게시글만 표시
    const maxPosts = 4;
    const visiblePosts = qnaPosts.slice(0, maxPosts);

    if (visiblePosts.length > 0) {
        // 문의게시글이 있을 경우
        noQnaImage.style.display = 'none'; // 이미지 숨김
        visiblePosts.forEach(post => {
            const postElement = document.createElement('div');
            postElement.classList.add('qna-post');
            postElement.innerHTML = `
                <h3>Q: ${post.question}</h3>
                <p>A: ${post.answer}</p>
            `;
            qnaContainer.appendChild(postElement);
        });
    } else {
        // 문의게시글이 없을 경우
        noQnaImage.style.display = 'block'; // 이미지 표시
        qnaContainer.innerHTML = ''; // 기존 게시글을 지웁니다.
    }
});

$(document).ready(function() {
    $('.box__contentList a').click(function(event) {
        event.preventDefault(); // 링크 이동 방지
        const newImageSrc = $(this).find('img').data('image');
        $('#main_img').attr('src', newImageSrc);
    });
});

// -----------------결제페이지 이동--------------------------------
document.getElementById("buyButton").addEventListener("click", function() {
    const productId = 123; // 상품 ID
    const productName = "퍼피아 핑카홀릭 하네스 리드줄";
    const productPrice = 30000;
    const productColor = "브라운";
    const productSize = "S";
    // ... 다른 상품 정보 추가하기 ...

    $.ajax({
        type: "GET", // 또는 "POST"
        url: "/productshop/pay",
        data: {
            productId: productId,
            productName: productName,
            productPrice: productPrice,
            productColor: productColor,
            productSize: productSize
            // ... 다른 상품 정보 ...
        },
        success: function(response) {
            // 결제 페이지로 이동
            window.location.href = "/productshop/pay?productId=" + productId + "&productName=" + productName + "&productPrice=" + productPrice + "&productColor=" + productColor + "&productSize=" + productSize; // GET 파라미터 추가
            // 또는
            // window.location.href = "/productshop/pay"; // POST 방식 (form submit과 함께)
        },
        error: function(error) {
            console.error("Error:", error);
            // 오류 처리 로직 추가
        }
    });
});