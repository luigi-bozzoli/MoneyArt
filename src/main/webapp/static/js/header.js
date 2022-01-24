$(document).ready(function(){
    $('.searchbar input[type = search]').attr('placeholder', 'Cerca opere...');
    $("#search-bar").attr('action', ctx + "/pages/cercaOpere.jsp");
    $('.switch input[type = checkbox]').click(function(){

        if ($('.switch input[type = checkbox]').is(':checked')) {
            $('.searchbar input[type = search]').attr('placeholder', 'Cerca artisti...');
            $("#search-bar").attr('action', ctx + "/pages/cercaUtenti.jsp");
        } else {
            $('.searchbar input[type = search]').attr('placeholder', 'Cerca opere...')
            $("#search-bar").attr('action', ctx + "/pages/cercaOpere.jsp");
        }
    });

    $("#search-bar").submit(function (){
        return onlyLetters($('.searchbar input[type = search]'));

    })



});


function onlyLetters(input) {
    let letters = /^[A-Za-z\s]{1,100}$/;

    if (input.val().match(letters) && !isEmpty(input.val())) {
        return true;
    } else {
        input.focus();
        return false;
    }
}
