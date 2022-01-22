function follow() {
    let params = {
        action : 'follow',
        followed : idArtista
    }

    $.get(ctx + '/follow', $.param(params), function(response) {
        if(response) {
            $('.follow').html('<a href="javascript:void(0)" class="btn" onclick="unfollow()">Smetti di seguire</a>');
            $('.follow').removeClass('follow').addClass('unfollow');
        }
    });
}

function unfollow() {
    let params = {
        action : 'unfollow'
    }

    $.get(ctx + '/follow', $.param(params), function(response) {
        if(response) {
            $('.unfollow').html('<a href="javascript:void(0)" class="btn" onclick="follow()">Segui</a>');
            $('.unfollow').removeClass('unfollow').addClass('follow');
        }
    });
}

$(document).ready(function() {
    $('.follow a').click(function () {
        follow();
    });

    $('.unfollow a').click(function () {
        unfollow();
    });
});