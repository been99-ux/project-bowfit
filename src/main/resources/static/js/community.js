$(document).ready(function () {
    $("#search-btn").click(function () {
        let input = $("#search-input");
        let cancelBtn = $("#cancel-btn");
        let searchConfirmBtn = $("#search-confirm-btn");

        if (input.hasClass("d-none")) {
            input.removeClass("d-none");
            cancelBtn.removeClass("d-none");
            searchConfirmBtn.removeClass("d-none");
            $(this).addClass("d-none"); // 돋보기 버튼 숨김
        }
    });

    $("#cancel-btn").click(function () {
        $("#search-input").addClass("d-none").val("");
        $("#search-btn").removeClass("d-none");
        $("#cancel-btn").addClass("d-none");
        $("#search-confirm-btn").addClass("d-none");
    });

    $("#search-confirm-btn").click(function () {
        alert("검색어: " + $("#search-input").val());
    });
});

document.addEventListener("DOMContentLoaded", function() {
    fetch('/admin/faq-notice/latest')
        .then(response => response.json())
        .then(notices => {
            const list = document.querySelector(".list-group");
            list.innerHTML = ""; // 기존 목록 비우기

            notices.forEach(notice => {
                const li = document.createElement("li");
                li.className = "list-group-item d-flex justify-content-between notice-list";
                li.innerHTML = `<span>${notice.title}</span> 
                                <span>${new Date(notice.createdAt).toLocaleDateString()}</span>`;
                list.appendChild(li);
            });
        })
        .catch(error => console.error("Error:", error));
});

document.addEventListener('DOMContentLoaded', function () {
    // 서버에서 최신 FAQ 데이터를 가져오는 함수
    function fetchLatestFaq() {
        fetch('/admin/faq-notice/latest-faq')  // 서버의 FAQ API URL
            .then(response => response.json())  // JSON 응답 받기
            .then(data => {
                // 데이터를 받아서 FAQ 리스트를 생성
                const faqList = document.getElementById('faqList');
                faqList.innerHTML = '';  // 기존의 항목들 초기화

                // 최신 FAQ 5개 항목을 반복하면서 HTML 생성
                data.forEach(faq => {
                    const listItem = document.createElement('li');
                    listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'faq-list');

                    // 제목과 날짜를 span에 추가
                    const titleSpan = document.createElement('span');
                    titleSpan.textContent = faq.title;

                    const dateSpan = document.createElement('span');
                    dateSpan.textContent = new Date(faq.createdAt).toISOString().split('T')[0];  // 날짜 포맷

                    // 제목과 날짜를 listItem에 추가
                    listItem.appendChild(titleSpan);
                    listItem.appendChild(dateSpan);

                    // listItem을 FAQ 목록에 추가
                    faqList.appendChild(listItem);
                });
            })
            .catch(error => {
                console.error('FAQ 데이터 가져오기 실패:', error);
            });
    }

    // 페이지 로드 후 최신 FAQ 항목 가져오기
    fetchLatestFaq();
});

// 여기서부터 QNA 자바스크립트 -----------------------------------------------------

$(document).ready(function () {
    // 페이지 로드 시 초기 설정 (공개글 선택 상태)
    $("input[name='password']").prop("disabled", true);

    // 라디오 버튼 변경 시 이벤트 핸들러
    $("input[name='form']").change(function () {
        if ($(this).val() === "public_form") {
            $("input[name='password']").val("").prop("disabled", true); // 비밀번호 입력 불가
        } else {
            $("input[name='password']").prop("disabled", false); // 비밀번호 입력 가능
        }
    });

    // 폼 제출 시 비밀글 여부를 hidden input으로 추가
    $("form").submit(function (e) {
        let secretValue = $("input[name='form']:checked").val() === "secret_form" ? "1" : "0";
        $("<input>").attr({
            type: "hidden",
            name: "secret",
            value: secretValue
        }).appendTo(this);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    fetch("/api/qna/latest")
        .then(response => response.json())
        .then(data => {
            const qnaList = document.getElementById("qna-list");
            qnaList.innerHTML = ""; // 기존 내용 초기화

            data.forEach(post => {
                const listItem = document.createElement("li");
                listItem.classList.add("list-group-item", "d-flex", "justify-content-between");

                // 제목 추가
                const title = document.createElement("span");
                title.textContent = post.title;
                listItem.appendChild(title);

                // 날짜 추가
                const date = document.createElement("span");
                date.classList.add("small-text");
                date.textContent = post.createdAt.substring(0, 10); // yyyy-MM-dd 형식
                listItem.appendChild(date);

                qnaList.appendChild(listItem);
            });
        })
        .catch(error => console.error("Q&A 게시물 불러오기 실패:", error));
});

