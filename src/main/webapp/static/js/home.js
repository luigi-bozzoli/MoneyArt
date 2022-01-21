$(document).ready(function(){
    $('.switch input[type = checkbox]').click(function(){

        if ($('.switch input[type = checkbox]').is(':checked')) {
            $('.searchbar input[type = search]').attr('placeholder', 'Cerca artisti...');
        } else {
            $('.searchbar input[type = search]').attr('placeholder', 'Cerca aste...')
        }
    });

});