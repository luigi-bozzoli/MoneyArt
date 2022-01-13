function checkEmail(input) {
    let email = /^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{1,100}$/;

    if (input.val().match(email))
        return true;

    input.focus();
    return false;
}

function onlyLetters(input) {
    let letters = /^[A-Za-z]+$/;

    if (input.val().match(letters)) {
        return true;
    } else {
        input.focus();
        return false;
    }
}

function isEmpty(str) {
    return (!str || str.length === 0);
}

function checkRepeatPw() {
    let pw = $("#password").val();
    let pwr = $("#repeat-password").val();

    if ((pw == pwr) || ((isEmpty(pw)) && isEmpty(pwr))) {
        return true;
    } else {
        return false;
    }
}

function checkPassword(pw) {
    let rightPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

    if (pw.val().match(rightPw) && checkRepeatPw()) {
        return true;
    } else {
        pw.focus();
        return false;
    }
}

function checkUsername(usr) {
    let rightusr = /^[A-Za-z0-9]{5,100}$/;
    if (usr.val().match(rightusr)) {
        return true;
    } else {
        usr.focus();
        return false;
    }
}


/* Regex login e signup*/

$(document).ready(function () {

    $(".signup").submit(function () {
        let name = onlyLetters($(".signup input[name = name]"));
        let surname = onlyLetters($(".signup input[name = surname]"));
        let email = checkEmail($(".signup input[name = email]"));
        let pw = checkPassword($(".signup input[name = password]"));
        let usr = checkUsername($(".signup input[name = username]"))
        let repeatPw = checkRepeatPw();

        if ((!name || !surname || !email || !usr) && pw) {
            $(".error").html('<p class="text-center">Dati inseriti non validi<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else if (!repeatPw) {
            $(".error").html('<p class="text-center">Le password non corrispondono<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else if (!pw) {
            $(".error").html('<p class="text-center">La password non è abbastanza complessa<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        }

    });

    $("#password, #repeat-password").on('keyup', function () {
        if (checkRepeatPw()) {
            $(".signup-input.pw").css("border", "none");
        } else {
            $(".signup-input.pw").css("border", "1px solid red");
        }
    });

});





