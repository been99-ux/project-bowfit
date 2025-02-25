$(document).ready(function() {
    // 전체 동의 체크박스 로직
    $('#agreeAll').change(function() {
        let checked = $(this).prop('checked');
        $('#terms1').prop('checked', checked);
        $('#terms2').prop('checked', checked);
        $('#terms3').prop('checked', checked);
    });

    // 회원가입 폼 제출 전 필수 입력 체크
    $('#signupForm').submit(function(event) {
        // 각 입력 필드 값 가져오기
        const loginId = $('#loginId').val();
        const password = $('#password1').val();
        const confirmPassword = $('#password2').val();
        const name = $('#name').val();
        const terms1 = $('#terms1').prop('checked');
        const terms2 = $('#terms2').prop('checked');

        // 필수 입력란 체크
        if (!loginId || !password || !confirmPassword || !name || !email) {
            event.preventDefault(); // 폼 전송 막기
            alert('모든 필수 항목을 입력해주세요.');
        } else if (password !== confirmPassword) {
            event.preventDefault();
            alert('비밀번호가 일치하지 않습니다.');
        } else if (!terms1 || !terms2) {
            event.preventDefault();
            alert('이용약관 동의는 필수입니다.');
        }
    });

    // 아이디 유효성 검사 및 중복 확인
    $('#loginId').on('input', function() {
        let loginId = $(this).val();

        $('#idValidationMessage').hide();
        $('#idDuplicateMessage').hide();
        $('#idAvailableMessage').hide();

        if (loginId.length === 0) {
            return;
        }

        $.ajax({
            type: 'GET',
            url: '/checkId?loginId=' + loginId,
            success: function(result) {
                if (result === 'invalid') {
                    $('#idValidationMessage').text('아이디는 영문소문자/숫자, 4~16자 이어야 합니다.').show();
                } else if (result === 'duplicate') {
                    $('#idDuplicateMessage').text('중복된 아이디입니다.').show();
                } else if (result === 'available') {
                    $('#idAvailableMessage').text('사용 가능한 아이디입니다.').show();
                }
            },
            error: function() {
                alert('아이디 중복 확인에 실패했습니다.');
            }
        });
    });

    // 비밀번호 유효성 검사
    $('#password1').on('input', function() {
        let password = $(this).val();

        $('#passwordValidationMessage').hide();

        if (password.length === 0) {
            return;
        }

        $.ajax({
            type: 'GET',
            url: '/checkPassword?password=' + password,
            success: function(result) {
                if (result === 'invalid') {
                    $('#passwordValidationMessage').text('영문소문자/숫자/특수문자 중 2가지 이상 조합해주세요.').show();
                } else if (result === 'valid') {
                    $('#passwordValidationMessage').hide();
                }
            },
            error: function() {
                alert('비밀번호 유효성 검사에 실패했습니다.');
            }
        });
    });

    // 비밀번호 일치 확인
    $('#password2').on('input', function() {
        let confirmPassword = $(this).val();

        $('#passwordMatchMessage').hide();

        if (confirmPassword.length === 0) {
            return;
        }

        checkPasswordMatch();
    });

    function checkPasswordMatch() {
        let password = $('#password1').val();
        let confirmPassword = $('#password2').val();
        if (password === confirmPassword) {
            $('#passwordMatchMessage').text('일치합니다.').css('color', 'green').show();
        } else {
            $('#passwordMatchMessage').text('일치하지 않습니다.').css('color', 'red').show();
        }
    }
});

$(document).ready(function () {
    $('#emailId, #domain-list').change(function () {
        const emailId = $('#emailId').val();
        const domain = $('#domain-list').val();
        const email = emailId + '@' + domain;
        $('#email').val(email);
    });
});

$(document).ready(function () {
    $('#phone1, #phone2, #phone3').on('input', function () {
        let phone1 = $('#phone1').val();
        let phone2 = $('#phone2').val();
        let phone3 = $('#phone3').val();

        // 하이픈(-) 자동 추가
        let phone = '';
        if (phone1.length > 0) {
            phone += phone1;
        }
        if (phone2.length > 0) {
            phone += '-' + phone2;
        }
        if (phone3.length > 0) {
            phone += '-' + phone3;
        }

        // phone hidden input field 에 값 설정
        $('#phone').val(phone);
    });
});

// 로그인 로그아웃
$(document).ready(function() {
    function updateLoginStatus() {
        $.ajax({
            type: 'GET',
            url: '/checkLoginStatus', // 로그인 상태 확인 URL
            success: function(data) {
                if (data.isLoggedIn) {
                    $('#loginButton').hide();
                    $('#logoutButton').show();
                    $('#userInfo').text(data.loginId + '님 환영합니다!');
                } else {
                    $('#loginButton').show();
                    $('#logoutButton').hide();
                    $('#userInfo').empty();
                }
            }
        });
    }

    updateLoginStatus(); // 페이지 로드 시 로그인 상태 확인

    // ... other JavaScript code
});
// 아이디 저장 메서드
$(document).ready(function() {
    // 아이디 저장 기능
    const rememberMe = $('#remember-me');
    const loginId = $('#loginId');

    // 쿠키에 저장된 아이디가 있다면 아이디 입력 필드에 자동 입력
    const savedLoginId = getCookie('loginId');
    if (savedLoginId) {
        loginId.val(savedLoginId);
        rememberMe.prop('checked', true);
    }

    rememberMe.change(function() {
        if ($(this).is(':checked')) {
            setCookie('loginId', loginId.val(), 30); // 30일 동안 쿠키 저장
        } else {
            deleteCookie('loginId'); // 쿠키 삭제
        }
    });

    // 쿠키 저장 함수
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = "expires=" + date.toUTCString();
        document.cookie = name + "=" + value + ";" + expires + ";path=/";
    }

    // 쿠키 삭제 함수
    function deleteCookie(name) {
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/";
    }

    // 쿠키 값 가져오기 함수
    function getCookie(name) {
        const nameEQ = name + "=";
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') c = c.substring(1, c.length);
            if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
        }
        return null;
    }
});

$(document).ready(function () {
    // 이메일 중복 검사
    $("#emailId, #domain-list").on("change", function () {
        let emailId = $("#emailId").val().trim();
        let domain = $("#domain-list").val();
        let email = emailId + "@" + domain;

        $("#email").val(email); // hidden input에도 저장

        if (emailId === "") {
            $("#emailMessage").css("color", "red").text("이메일을 입력하세요.");
            return;
        }

        $.ajax({
            type: "POST",
            url: "/check-email",
            data: { email: email },
            success: function (response) {
                if (response.exists) {
                    $("#emailMessage").css("color", "red").text("중복된 이메일입니다.");
                } else {
                    $("#emailMessage").css("color", "green").text("사용 가능한 이메일입니다.");
                }
            },
            error: function () {
                $("#emailMessage").css("color", "red").text("서버 오류가 발생했습니다.");
            }
        });
    });

    // 휴대폰 번호 중복 검사
    $("#phone1, #phone2, #phone3").on("input", function () {
        let phone1 = $("#phone1").val().trim();
        let phone2 = $("#phone2").val().trim();
        let phone3 = $("#phone3").val().trim();

        if (phone1.length === 3 && phone2.length === 4 && phone3.length === 4) {
            let phone = phone1 + "-" + phone2 + "-" + phone3;
            $("#phone").val(phone); // hidden input에도 저장

            $.ajax({
                type: "POST",
                url: "/check-phone",
                data: { phone: phone },
                success: function (response) {
                    if (response.exists) {
                        $("#phoneMessage").css("color", "red").text("중복된 번호입니다.");
                    } else {
                        $("#phoneMessage").css("color", "green").text("사용 가능한 번호입니다.");
                    }
                },
                error: function () {
                    $("#phoneMessage").css("color", "red").text("서버 오류가 발생했습니다.");
                }
            });
        }
    });
});

$(document).ready(function () {
    $("#signupForm").submit(function (event) {
        let emailId = $("#emailId").val().trim();
        let domain = $("#domain-list").val().trim();
        let phone1 = $("#phone1").val().trim();
        let phone2 = $("#phone2").val().trim();
        let phone3 = $("#phone3").val().trim();

        // 이메일과 휴대폰 번호 유효성 검사
        if (emailId === "") {
            alert("이메일을 입력해주세요.");
            $("#emailId").focus();
            event.preventDefault(); // 폼 제출 방지
            return;
        }

        if (phone1 === "" || phone2 === "" || phone3 === "") {
            alert("휴대폰 번호를 입력해주세요.");
            $("#phone1").focus();
            event.preventDefault(); // 폼 제출 방지
            return;
        }
    });
});
// 회원가입 완료 메시지 출력
$(document).ready(function() {
    $("#signupForm").submit(function(event) {
        event.preventDefault(); // 폼 자동 전송 막기

        $.ajax({
            type: "POST",
            url: "/join",
            data: $(this).serialize(), // 폼 데이터 전송
            success: function(response) {
                alert("회원가입이 완료되었습니다.");
                window.location.href = "/login"; // 로그인 페이지로 이동
            },
            error: function(error) {
                alert("회원가입에 실패했습니다. 다시 시도해주세요.");
                console.error("Error:", error); // 콘솔에 에러 메시지 출력
            }
        });
    });
});



