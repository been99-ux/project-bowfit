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