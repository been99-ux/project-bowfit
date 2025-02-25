$(document).ready(function () {
    let currentPage = 0;  // 현재 페이지
    let pageSize = 15;  // 한 페이지당 게시글 개수
    let openPostId = null;  // 현재 열려있는 게시글 ID 추적 변수

    // 현재 페이지가 FAQ인지 NOTICE인지 확인
    let pageType = window.location.pathname.includes("faq") ? "faq" : "notice";

    // 날짜 형식 포맷팅 함수
    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}. ${month}. ${day}`;
    }

    // 게시글 목록 로드 (검색 포함)
    function loadPosts(page, query = "") {
        let url = query
            ? `/admin/faq-notice/search?topic=${pageType}&query=${query}&page=${page}&size=${pageSize}`
            : `/admin/faq-notice/list/${pageType}?page=${page}&size=${pageSize}`;

        $.ajax({
            url: url,
            method: "GET",
            success: function (data) {
                let tbodyContent = "";
                data.content.forEach(function (post, index) {
                    let titleWithImageTag = post.title;
                    if (post.imageUrl) {
                        titleWithImageTag += " (사진)";
                    }

                    tbodyContent += `
                <tr>
                    <td>${index + 1 + (page * pageSize)}</td>
                    <td>
                        <span class="badge bg-secondary view-btn" data-id="${post.id}">VIEW</span>
                        ${titleWithImageTag} 
                    </td>
                    <td>${post.writerName.replace(/^(.)(.*)$/, (match, first, rest) => first + '*'.repeat(rest.length))}</td>
                    <td>${formatDate(post.createdAt)}</td>
                </tr>
                <tr class="post-preview" id="post-preview-${post.id}" style="display: none;">
                    <td colspan="4">
                        <div class="view_background">
                            <div class="alert alert-info">
                             ${post.content.replace(/\./g, '.<br>')}
                            </div class="notice_image">
                            ${post.imageUrl ? `<img src="${post.imageUrl}" class="img-fluid" style="max-width: 100%; height: auto;">` : ''}
                        </div>
                    </td>
                </tr>`;
                });

                $("#posts-table-body").html(tbodyContent);
                updatePagination(data, query);
            }
        });
    }

    // 페이지네이션 업데이트
    function updatePagination(data, query) {
        let paginationHtml = `
            <li class="page-item ${data.first ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${data.number - 1}" data-query="${query}">Previous</a>
            </li>`;

        for (let i = 0; i < data.totalPages; i++) {
            paginationHtml += `
            <li class="page-item ${i === data.number ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}" data-query="${query}">${i + 1}</a>
            </li>`;
        }

        paginationHtml += `
            <li class="page-item ${data.last ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${data.number + 1}" data-query="${query}">Next</a>
            </li>`;

        $(".pagination").html(paginationHtml);
    }

    // 검색 실행 함수
    function executeSearch() {
        let searchQuery = $("#search-query").val().trim();
        loadPosts(0, searchQuery);
    }

    // 검색 버튼 클릭 이벤트
    $("#search-button").click(function () {
        executeSearch();
    });

    // **🔹 엔터 키 입력 시 검색 실행**
    $("#search-query").keypress(function (event) {
        if (event.which === 13) {  // Enter 키(13) 감지
            event.preventDefault();  // 폼 자동 제출 방지
            executeSearch();
        }
    });

    // VIEW 버튼 클릭 이벤트 (토글)
    $(document).on("click", ".view-btn", function () {
        var postId = $(this).data("id");
        var postPreviewRow = $("#post-preview-" + postId);

        if (openPostId === postId) {
            postPreviewRow.slideUp();
            openPostId = null;
        } else {
            $(".post-preview").slideUp();
            postPreviewRow.slideDown();
            openPostId = postId;
        }
    });

    // 페이지네이션 클릭 이벤트 (검색 유지)
    $(document).on("click", ".pagination .page-link", function (e) {
        e.preventDefault();
        let selectedPage = $(this).data("page");
        let query = $("#search-query").val().trim();
        if (selectedPage >= 0) {
            currentPage = selectedPage;
            loadPosts(currentPage, query);
        }
    });

    loadPosts(currentPage);
});
