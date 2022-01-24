function checkEmail(input) {
    let email = /^[A-z0-9._%+-]+@[A-z0-9.-]+\.[A-z]{1,100}$/;

    if (input.val().match(email) && !isEmpty(input.val()))
        return true;

    input.focus();
    return false;
}

function onlyLetters(input) {
    let letters = /^[A-Za-z\s]{1,100}$/;

    if (input.val().match(letters) && !isEmpty(input.val())) {
        return true;
    } else {
        input.focus();
        return false;
    }
}

function isEmpty(str) {
    return (!str || str.length === 0);
}


function checkPassword(pw) {
    let rightPw = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;

    if (pw.val().match(rightPw) && !isEmpty(pw.val())) {
        return true;
    } else {
        pw.focus();
        return false;
    }
}

function checkUsername(usr) {
    let rightusr = /^[A-Za-z0-9]{5,100}$/;
    if (usr.val().match(rightusr) && !isEmpty(usr.val())) {
        return true;
    } else {
        usr.focus();
        return false;
    }
}

function checkProfile(foto){
    if(foto[0].files[0] === undefined){
        return true
    }

   return foto[0].files[0].size < 10000000;
}

function checkExtension(foto){
    if(foto[0].files[0] === undefined){
        return true
    }
    var filename = foto.val();

    var pathParts = filename.split('/');   // Split the path on '/': ['path', 'to', 'file.ext']
    var filename  = pathParts.pop();       // Take the last element. This is a file name: 'file.ext'
    var filenameParts = filename.split('.'); // Split the file name on the '.': ['file', 'ext']
    var extension = filenameParts[1];
    console.log(extension);
    return extension === "jpg" || extension === "png";
}

function preview( uploader ) {
    if ( uploader.files && uploader.files[0] ){
        $('.propic img').attr('src',
            window.URL.createObjectURL(uploader.files[0]) );
    }
}

$(document).ready(function () {
    let pw = false;
    let img = $('.propic img').attr('src');

    $('#update-profile').click(function () {
        $('.info, .balance-info, .followers-info, .following, .unfollow').removeClass('d-flex').addClass('d-none');
        $('.update-informations').removeClass('d-none');
        $('#update-choice').removeClass('d-none').addClass('d-flex');
        $('#update-profile').addClass('d-none');
        $('.overlay').removeClass('d-none').css({'cursor': 'pointer', 'pointer-events' : 'auto'});
        $('.info-propic').html('<p>Per cambiare la foto profilo clicca sull\'immagine.</p>');
    });
    $('#cancel-update').click(function () {
        $('.info').removeClass('d-none')    ;
        $('.balance-info, .followers-info, .following, .unfollow').removeClass('d-none').addClass('d-flex');
        $('.update-informations').addClass('d-none');
        $('#update-choice').removeClass('d-flex').addClass('d-none');
        $('#update-profile').removeClass('d-none');
        $('#password, #new-password, #confirm-password').val('');

        if(!$('.pw').hasClass('d-none')) {
            $('.pw').addClass('d-none');
            $('.pwc').removeClass('d-none');
        }
        $('#update-password').removeClass('d-none');
        $('.data-input.pwc').css('border', 'none');
        $('.data-input.pw').css('border', 'none');
        $('.pass-info').addClass('d-none');

        $('.overlay').addClass('d-none').css({'cursor': 'auto', 'pointer-events' : 'none'});
        $('.propic img').attr('src', img);
        $('.info-propic').html('');
        $('#name').val($('#name').attr('value'));
        $('#surname').val($('#surname').attr('value'));
        $('#email').val($('#email').attr('value'));
        $('#username').val($('#username').attr('value'));
        $(".error").html('');
        window.scrollTo(0, 0);
    });

    $('#update-password').click(function (event) {
        $('.pw').removeClass('d-none');
        $('.pwc').addClass('d-none');
        $('#update-password').addClass('d-none');
        $('.pass-info').removeClass('d-none');
        if(!$('#confirm-update').hasClass('disabled')) {
            $('#confirm-update').addClass('disabled');
        }
    });

    $('#confirm-password').keyup(function () {
        let value = $('#confirm-password').val();
        if(value.length > 2) {
            $.post(ctx+'/changeUserInformation', $.param({'password' : value}), function (response) {
                if(response) {
                    $('.data-input.pwc').css('border', 'none');
                    $('#confirm-update').removeClass('disabled');
                } else {
                    $('.data-input.pwc').css('border', '1px solid red');

                    if(!$('#confirm-update').hasClass('disabled')) {
                        $('#confirm-update').addClass('disabled');
                    }
                }
            });
        }
    });

    $('#password').keyup(function () {
        let value = $('#password').val();
        if(value.length > 2) {
            $.post(ctx+'/changeUserInformation', $.param({'password' : value}), function (response) {
                if(response) {
                    if(checkPassword($('#new-password'))) {
                        $('.data-input.pw').css('border', 'none');
                        $('#confirm-update').removeClass('disabled');
                    }
                    pw = true;
                } else {
                    $('.data-input.pw').css('border', '1px solid red');

                    if(!$('#confirm-update').hasClass('disabled')) {
                        $('#confirm-update').addClass('disabled');
                    }
                    pw = false;
                }
            });
        }
    });

    $('#new-password').keyup(function () {
        if(checkPassword($('#new-password')) && pw) {
            $('.data-input.pw').css('border', 'none');
            $('#confirm-update').removeClass('disabled');
        } else {
            $('.data-input.pw').css('border', '1px solid red');

            if(!$('#confirm-update').hasClass('disabled')) {
                $('#confirm-update').addClass('disabled');
            }
        }
    });

    $('.overlay').click(function (event) {
        $('#propicForm').click();
    });

    $('#propicForm').change(function(){
        preview( this );
    });

    $('#confirm-update').click(function() {
        let name = onlyLetters($(".data input[name = name]"));
        let surname = onlyLetters($(".data input[name = surname]"));
        let email = checkEmail($(".data input[name = email]"));
        let pw = checkPassword($(".data input[name = password]"));
        let usr = checkUsername($(".data input[name = username]"));
        let foto = checkProfile($("#propicForm"));
        let estensione = checkExtension($("#propicForm"));

        if ((!name || !surname || !email || !usr)) {
            $(".error").html('<p class="text-center">Dati inseriti non validi<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else if (!foto || !estensione) {
            $(".error").html('<p class="text-center">Foto non valida<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
        } else {
            $('.data').submit();
        }
    });


});