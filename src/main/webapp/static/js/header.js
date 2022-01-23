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

});