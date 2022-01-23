$(document).ready(function () {

    $(".timer").each(function () {

            let countdown = Date.parse($(this).text())
            let element = $(this)

            let x = setInterval(function() {

                // Get todays date and time
                let now = new Date().getTime();
                //console.log(countdown)


                // Find the distance between now an the count down date
                let distance = countdown - now;

                // Time calculations for days, hours, minutes and seconds
                let days = Math.floor(distance / (1000 * 60 * 60 * 24))
                let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((distance % (1000 * 60)) / 1000);

                if(days.toString().length == 1) {
                    days = '0' + days;
                }
                if(hours.toString().length == 1) {
                    hours = '0' + hours;
                }
                if(minutes.toString().length == 1) {
                    minutes = '0' + minutes;
                }
                if(seconds.toString().length == 1) {
                    seconds = '0' + seconds;
                }



                //console.log(distance)
                // If the count down is over, write some text
                if (distance < 0) {
                    clearInterval(x);
                    element.html('Scaduta');
                } else {
                    element.html(days + ':' + hours + ':' + minutes + ':' + seconds);
                }
            }, 1000);
        }
    )

    $(".bottone-in-attesa").click(function (){
        let astaId = $(this).val();
        let elem = $("#in-attesa-" + astaId)
        $.get(ctx + '/cancelAuction?id=' + astaId, function (asta) {
            elem.remove();
        })
    })

    $(".bottone-in-corso").click(function (){
        let astaId = $(this).val();
        let elem = $("#in-corso-" + astaId)
        $.get(ctx + '/cancelAuction?id=' + astaId, function (asta) {
            elem.remove();
        })
    })
})