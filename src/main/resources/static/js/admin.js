document.querySelector(".jsFilter").addEventListener("click", function () {
    document.querySelector(".filter-menu").classList.toggle("active");
});

document.querySelector(".grid").addEventListener("click", function () {
    document.querySelector(".list").classList.remove("active");
    document.querySelector(".grid").classList.add("active");
    document.querySelector(".products-area-wrapper").classList.add("gridView");
    document
        .querySelector(".products-area-wrapper")
        .classList.remove("tableView");
});

document.querySelector(".list").addEventListener("click", function () {
    document.querySelector(".list").classList.add("active");
    document.querySelector(".grid").classList.remove("active");
    document.querySelector(".products-area-wrapper").classList.remove("gridView");
    document.querySelector(".products-area-wrapper").classList.add("tableView");
});

var modeSwitch = document.querySelector('.mode-switch');
modeSwitch.addEventListener('click', function () {
    document.documentElement.classList.toggle('light');
    modeSwitch.classList.toggle('active');
});


// ... (your existing JavaScript code) ...

const sidebarItems = document.querySelectorAll('.sidebar-list-item');
const contentAreas = document.querySelectorAll('.content-area');
const headerText = document.querySelector('.app-content-headerText');

sidebarItems.forEach(item => {
    item.addEventListener('click', () => {
        const target = item.dataset.target;

        contentAreas.forEach(area => area.classList.remove('active'));
        document.getElementById(target).classList.add('active');

        sidebarItems.forEach(si => si.classList.remove('active'));
        item.classList.add('active');

        headerText.textContent = target.charAt(0).toUpperCase() + target.slice(1);

        // "products" 영역이 활성화되었을 때만 버튼 추가
        const productsArea = document.getElementById('products');
        if (target === 'products' && !productsArea.contains(addProductButton)) {
            productsArea.appendChild(addProductButton);
        } else if (target !== 'products' && productsArea.contains(addProductButton)) {
            productsArea.removeChild(addProductButton);
        }
    });
});

// 초기 활성화 콘텐츠 설정 및 "Add Product" 버튼 추가
// document.getElementById('products').classList.add('active');

document.addEventListener('DOMContentLoaded', function() {
    const activeTab = localStorage.getItem('activeTab') || 'products';
    document.getElementById(activeTab).classList.add('active');
    headerText.textContent = activeTab.charAt(0).toUpperCase() + activeTab.slice(1);
});

// 탭 클릭 시 활성화된 탭을 localStorage에 저장
sidebarItems.forEach(item => {
    item.addEventListener('click', () => {
        const target = item.dataset.target;
        localStorage.setItem('activeTab', target);
    });
});

document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('createPostForm');

    form.addEventListener('submit', function(event) {
        var title = document.getElementById('titleInput').value.trim();
        var content = document.getElementById('contentInput').value.trim();

    });
});


//------------------------------------------------------------------------------------------
$(document).ready(function () {
    let currentPage = 0; // 현재 페이지
    const pageSize = 6;  // 한 페이지당 표시할 게시물 수

    // 🔹 게시글 목록 불러오기
    function loadPosts(page, query = '', topic = '') {
        $.ajax({
            url: '/admin/faq-notice/posts',
            method: 'GET',
            data: { page: page, size: pageSize, query: query, topic: topic },
            success: function (data) {
                let html = '';
                data.content.forEach(function (post) {
                    html += `
                        <div class="product-row" style="display: flex">
                            <div class="product-cell category">${post.id}</div>
                            <div class="product-cell sales">${post.topic}</div>
                            <div class="product-cell stock">${post.writerName}</div>
                            <div class="product-cell price">${post.title}</div>
                            <div class="detail_info">
                                <a href="#" class="view-btn" data-id="${post.id}" style="text-decoration-line: none">view</a>
                            </div>
                        </div>
                    `;
                });
                $('#faq-notice-list').html(html);
                updatePagination(data);
            },
            error: function () {
                alert('게시글 목록을 불러오는 데 실패했습니다.');
            }
        });
    }

    // 🔹 페이지네이션 업데이트
    function updatePagination(data) {
        let paginationHtml = '';
        if (data.number > 0) {
            paginationHtml += `<a href="#" class="prev" data-page="${data.number - 1}">이전</a>`;
        }
        paginationHtml += `<span> ${data.number + 1} / ${data.totalPages}</span>`;
        if (data.number < data.totalPages - 1) {
            paginationHtml += `<a href="#" class="next" data-page="${data.number + 1}">다음</a>`;
        }
        $('.community_member_paging').html(paginationHtml);
    }

    // 🔹 초기 게시글 로드
    loadPosts(currentPage);

    // 🔹 페이지 이동
    $(document).on('click', '.prev, .next', function () {
        currentPage = $(this).data('page');
        loadPosts(currentPage, $('#searchQuery').val(), $('#searchTopic').val());
    });

    // 🔹 검색 기능
    $('#searchForm').submit(function (event) {
        event.preventDefault();
        let query = $('#searchQuery').val();
        let topic = $('#searchTopic').val();
        currentPage = 0; // 검색 시 첫 페이지로 이동
        loadPosts(currentPage, query, topic);
    });

    // 🔹 게시글 상세 조회 (모달 열기)
    $(document).on('click', '.view-btn', function (event) {
        event.preventDefault();
        let postId = $(this).data('id');

        $.ajax({
            url: `/admin/faq-notice/post/${postId}`,
            method: 'GET',
            success: function (post) {
                $('#modalPostId').val(post.id);
                $('#modalTitle').val(post.title);
                $('#modalContent').val(post.content);
                $('#modalDate').text(`작성 날짜: ${new Date(post.createdAt).toLocaleDateString()}`);

                if (post.imageUrl) {
                    $('#modalImage').attr('src', post.imageUrl).hide();
                } else {
                    $('#modalImage').hide();
                }

                $('#postModal').fadeIn();
            },
            error: function () {
                alert('게시글 정보를 불러오는 데 실패했습니다.');
            }
        });
    });

    // 🔹 모달 닫기 (입력값 초기화)
    $('#closeModal, .close').on('click', function () {
        $('#modalPostId').val('');
        $('#modalTitle').val('');
        $('#modalContent').val('');
        $('#modalImage').hide();
        $('#postModal').fadeOut();
    });

    // 🔹 게시글 수정
    $('#savePost').on('click', function () {
        let postId = $('#modalPostId').val();
        let updatedTitle = $('#modalTitle').val();
        let updatedContent = $('#modalContent').val();

        $.ajax({
            url: `/admin/faq-notice/post/${postId}`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ title: updatedTitle, content: updatedContent }),
            success: function () {
                alert('게시물이 수정되었습니다.');
                $('#postModal').fadeOut();
                loadPosts(currentPage); // 현재 페이지 유지하며 목록 새로고침
            },
            error: function () {
                alert('수정 실패.');
            }
        });
    });

    // 🔹 게시글 삭제
    $('#deletePost').on('click', function () {
        let confirmed = confirm('정말로 삭제하시겠습니까?');
        if (confirmed) {
            let postId = $('#modalPostId').val();

            $.ajax({
                url: `/admin/faq-notice/post/${postId}`,
                method: 'DELETE',
                success: function () {
                    alert('게시물이 삭제되었습니다.');
                    $('#postModal').fadeOut();

                    // 삭제 후 현재 페이지에 게시글이 남아있는지 확인
                    $.ajax({
                        url: '/admin/faq-notice/posts',
                        method: 'GET',
                        data: { page: currentPage, size: pageSize },
                        success: function (data) {
                            if (data.content.length === 0 && currentPage > 0) {
                                currentPage--; // 마지막 게시물 삭제 시 이전 페이지로 이동
                            }
                            loadPosts(currentPage);
                        }
                    });
                },
                error: function () {
                    alert('삭제 실패.');
                }
            });
        }
    });
});




//-----------------------------------------------------------------------------------------------

// 관리자 페이지 게시물 작성 완료 팝업 후 관리자 페이지 다시 리턴
document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("createPostForm");

    form.addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지 (AJAX로 처리하기 위해)

        let formData = new FormData(form); // 폼 데이터 가져오기

        fetch("/admin/faq-notice/create", {
            method: "POST",
            body: formData
        })
            .then(response => response.text()) // 서버 응답을 텍스트로 받기
            .then(result => {
                if (result === "success") {
                    alert("게시글이 작성 완료되었습니다."); // ✅ alert 띄우기
                    window.location.href = "/adminpage";
                } else {
                    alert("게시글 작성 실패: " + result); // 실패 시 오류 메시지 출력
                }
            })
            .catch(error => console.error("Error:", error));
    });
});

