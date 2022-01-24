


$(document).ready(function (){

    console.log("pro")
    $("prelievo-button").click(function (){

        if($("#prelievo-amount").val() <= 0 ){
            return false;
        }
    })
});