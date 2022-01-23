$(document).ready(function () {
    $(".data button").click(function (e) {
        e.preventDefault();
        let inizio = new Date($(".data input[name = inizio]").val()).setHours(0,0,0,0);
        let fine = new Date($(".data input[name = fine]").val()).setHours(0,0,0,0);
        let currentDate = new Date().setHours(0,0,0,0);

        if (inizio < currentDate || fine < currentDate || fine < inizio ) {
            $(".error").html('<p class="text-center">Formato date errate!<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else if (inizio == fine) {
            $(".error").html(`<p class="text-center">L'asta deve durare almeno un giorno!<i class="fas fa-exclamation-triangle ml-2"></i></p>`);
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else {
            $('.data').submit();
        }
    });
});