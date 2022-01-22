$(document).ready(function (){
    let x = setInterval(function() {

        $(".item-price").each(function (){
            let id = $(this).find("input").val();
            let elem = $(this)
            $.get(ctx + '/getArtworkValue', $.param({'id' : id}), function(response) {
                let value = response
                let input = elem.find("input").clone();

                elem.html( value.toFixed(2) + "€")
                elem.append(input)
            })
        })
    },1000);
})