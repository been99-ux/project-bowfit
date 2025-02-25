document.addEventListener('DOMContentLoaded', function() {
    // 모달 열기 및 탭 전환 기능
    document.querySelectorAll('.open-modal').forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();  // 링크 클릭 시 페이지 리로드 방지

            const modal = document.getElementById('modal');
            const tabName = this.dataset.tab;

            // 모달 열기
            modal.style.display = 'flex';  // 모달을 보이도록 설정

            // 탭 전환
            switchTab(tabName);
        });
    });

    // 탭 클릭 시 전환
    document.querySelectorAll('.tablinks').forEach(tab => {
        tab.addEventListener('click', function() {
            const tabName = this.dataset.tab;
            switchTab(tabName);
        });
    });

    function switchTab(tabName) {
        // 모든 탭내용 숨기기
        document.querySelectorAll('.tabcontent').forEach(content => {
            content.classList.remove('active');
            content.style.display = 'none';  // 비활성화된 콘텐츠 숨기기
        });

        // 모든 탭버튼 비활성화
        document.querySelectorAll('.tablinks').forEach(tab => {
            tab.classList.remove('active');
        });

        // 선택된 탭 활성화
        const activeContent = document.getElementById(tabName);
        activeContent.classList.add('active');
        activeContent.style.display = 'block';  // 활성화된 콘텐츠 보이기
        document.querySelector(`.tablinks[data-tab="${tabName}"]`).classList.add('active');

        // h2 제목 변경
        const title = tabName === 'findId' ? '아이디 찾기' : '비밀번호 찾기';
        document.getElementById('tabTitle').textContent = title;
    }

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', function(event) {
        const modal = document.getElementById('modal');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});
