$(document).ready(function (){
    $('#myTabContent').addClass("loading");
    setTimeout(function() {
        $('#myTabContent').removeClass("loading");
    }, 1000);

    $.get(ctx + '/getAuctions', $.param({'action' : 'inCorso'}), function(response) {
        $.each(response, function (index, asta) {
            let countdown = new Date(asta.dataFine).getTime();
            let idAsta = asta.id;
            let x = setInterval(function() {

                // Get todays date and time
                let now = new Date().getTime();

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

                $('#' + idAsta + ' .timer').html(days + ':' + hours + ':' + minutes + ':' + seconds);

                // If the count down is over, write some text
                if (distance < 0) {
                    clearInterval(x);
                    $('#' + idAsta + ' .timer').html('Scaduta');
                }
            }, 1000);
        });
    });

    $('.dropdown-item').click(function () {
        let text = $(this).find('p').text();
        let icon = $(this).find('i').attr('class')

        $('#sort-text').html(text);
        $('#sort-icon').attr('class', icon);
        $('#close-drop').removeClass('invisible');

        if(text == 'Popolarità artista') {
            text = 'Followers'
        }

        if(icon.includes('up')) {
            icon = 'ASC'
        } else {
            icon = 'DESC'
        }

        let parameters = {
            action : 'inCorso',
            criteria : text,
            order : icon
        }

        $.get(ctx + '/getAuctions', $.param(parameters), function(response) {
            $('#container-aste').html('');
            $.each(response, function (index, asta) {
                let prezzo
                if(asta.partecipazioni.length == 0) {
                    prezzo = 'Nessuna offerta';
                } else {
                    prezzo = asta.partecipazioni[(asta.partecipazioni.length - 1)].offerta.toFixed(2);
                    prezzo = prezzo.toString().replace('.', ',');
                    prezzo = '€ '.concat(prezzo);
                }

                $('#myTabContent').addClass("loading");
                setTimeout(function() {
                    $('#myTabContent').removeClass("loading");
                }, 1000);

                $('#container-aste').append(
                    `<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">` +
                        `<div class="thumb-wrapper">` +
                            `<div class="img-box">` +
                                `<img src="` + ctx + `/artworkPicture?id=` + asta.opera.id + `"`  +
                                    `class="img-responsive">` +
                            `</div>` +
                            `<div class="thumb-content">` +
                                `<h4>` + asta.opera.nome +`</h4>` +
                                `<div class="expiration-timer" id="` + asta.id +`">` +
                                   `<span class="timer"></span>` +
                                `</div>` +
                                `<p class="item-price">` + prezzo + `</p>` +
                                `<a href="#" class="btn btn-primary">Vai all'asta</a>` +    //todo aggiungere il link alla pagina dell'asta
                            `</div>` +
                        `</div>` +
                    `</div>`


                );
            });
        });
    });

    $('#close-drop').click(function (){
        $('#sort-text').html('');
        $('#sort-icon').attr('class', 'fas fa-sort');
        $('#close-drop').addClass('invisible');

        $.get(ctx + '/getAuctions', $.param({action : 'inCorso'}), function(response) {
            $('#container-aste').html('');
            $.each(response, function (index, asta) {
                let prezzo
                if(asta.partecipazioni.length == 0) {
                    prezzo = 'Nessuna offerta';
                } else {
                    prezzo = asta.partecipazioni[(asta.partecipazioni.length - 1)].offerta.toFixed(2);
                    prezzo = prezzo.toString().replace('.', ',');
                    prezzo = '€ '.concat(prezzo);
                }

                $('#myTabContent').addClass("loading");
                setTimeout(function() {
                    $('#myTabContent').removeClass("loading");
                }, 1000);

                $('#container-aste').append(
                    `<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">` +
                        `<div class="thumb-wrapper">` +
                            `<div class="img-box">` +
                                `<img src="` + ctx + `/artworkPicture?id=` + asta.opera.id + `"`  +
                                `class="img-responsive">` +
                            `</div>` +
                            `<div class="thumb-content">` +
                                `<h4>` + asta.opera.nome +`</h4>` +
                                `<div class="expiration-timer" id="` + asta.id +`">` +
                                    `<span class="timer"></span>` +
                                `</div>` +
                                `<p class="item-price">` + prezzo + `</p>` +
                                `<a href="#" class="btn btn-primary">Vai all'asta</a>` +    //todo aggiungere il link alla pagina dell'asta
                            `</div>` +
                        `</div>` +
                    `</div>`

                );
            });
        });
    });
});



