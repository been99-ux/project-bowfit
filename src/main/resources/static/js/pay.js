// 토글
function toggleVisibility(id) {
    var content = document.getElementById(id);

    if (content.style.display === "none" || content.style.display === "") {
        content.style.display = "block"; // 보이게 설정
    } else {
        content.style.display = "none"; // 숨기기
    }
}

//  주소 시작
function sample6_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = data.userSelectedType === 'R' ? data.roadAddress : data.jibunAddress;
            var extraAddr = '';

            if (data.userSelectedType === 'R') {
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                document.getElementById("sample6_extraAddress").value = extraAddr;
            } else {
                document.getElementById("sample6_extraAddress").value = '';
            }

            document.getElementById('sample6_postcode').value = data.zonecode;
            document.getElementById("sample6_address").value = addr;
            document.getElementById("sample6_detailAddress").focus();
        }
    }).open();
}

// 주소 끝

$(document).ready(function() {
    if ($.fn.owlCarousel) {  // ✅ 플러그인이 존재하는지 확인 후 실행
        $(".owl-carousel").owlCarousel({
            loop: true,
            margin: 10,
            nav: true,
            items: 3
        });
    } else {
        console.error("Owl Carousel is not loaded!");
    }
});