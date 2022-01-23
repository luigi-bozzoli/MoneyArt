function copyUrl() {
    if (!window.getSelection) {
        alert('Please copy the URL from the location bar.');
        return;
    }
    const dummy = document.createElement('p');
    dummy.textContent = window.location.href;
    document.body.appendChild(dummy);

    const range = document.createRange();
    range.setStartBefore(dummy);
    range.setEndAfter(dummy);

    const selection = window.getSelection();
    // First clear, in case the user already selected some other text
    selection.removeAllRanges();
    selection.addRange(range);

    document.execCommand('copy');
    document.body.removeChild(dummy);
}

$(document).ready(function() {

    $('#share').click(function () {
        copyUrl();
        $('#share i').attr("class", "fas fa-check");
        $('#share p').html('Link <br> copiato!');
        setTimeout(function () {
            $('#share i').attr("class", "fas fa-share-alt");
            $('#share p').html('Condividi');
        }, 3000);
    });

    $('.modal-content button[type = submit]').click(function (event) {
        event.preventDefault();
        let params = {
            asta : $('.modal-content input[name = asta]').val(),
            commento : $('.modal-content textarea').val()
        }

        $.post(ctx+'/addReport', $.param(params), function(response) {
            if(response) {
                $('.modal-content button[type = button]').click();
                $('#report i').attr("class", "fas fa-check");
                $('#report p').html('Segnalato!');
            } else {
                $('.message').html(`<p class="mt-3" style="color: #BB371A !important;">Problema con la segnalazione</p>`);
            }
        });
    });

    if (document.URL.includes("getAuction") || document.URL.includes("newOffer") || document.URL.includes("addReport")) {
        let astaCorrente;
        $.get(ctx + '/getAuction?id=' + astaId, function (asta) {
            let countdown = new Date(asta.dataFine).getTime();
            astaCorrente = asta;
            let x = setInterval(function () {

                // Get todays date and time
                let now = new Date().getTime();

                // Find the distance between now an the count down date
                let distance = countdown - now;

                // Time calculations for days, hours, minutes and seconds
                let days = Math.floor(distance / (1000 * 60 * 60 * 24))
                let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((distance % (1000 * 60)) / 1000);

                if (days.toString().length == 1) {
                    days = '0' + days;
                }
                if (hours.toString().length == 1) {
                    hours = '0' + hours;
                }
                if (minutes.toString().length == 1) {
                    minutes = '0' + minutes;
                }
                if (seconds.toString().length == 1) {
                    seconds = '0' + seconds;
                }

                $('.days h5').html(days);
                $('.hours h5').html(hours);
                $('.minutes h5').html(minutes);
                $('.seconds h5').html(seconds);

                // If the count down is over, write some text
                if (distance < 0) {
                    clearInterval(x);
                    $('.timer-info').html('Asta scaduta');
                }

                let prezzo;
                if (asta.partecipazioni.length == 0) {
                    prezzo = 'Nessuna offerta';
                } else {
                    prezzo = asta.partecipazioni[(asta.partecipazioni.length - 1)].offerta.toFixed(2);
                    prezzo = prezzo.toString().replace('.', ',');
                    prezzo = '€ '.concat(prezzo);
                }
            }, 1000);
            $('.best-offer h5').html(prezzo);
        });

        $('.offer-input input[name = offerta]').keyup(function () {
            $.get(ctx + '/newOffer', function (utente) {
                let prezzo;
                if (astaCorrente.partecipazioni.length == 0) {
                    prezzo = 0;
                } else {
                    prezzo = astaCorrente.partecipazioni[(astaCorrente.partecipazioni.length - 1)].offerta;
                }

                let offerta = $('.offer-input input[name = offerta]').val();


                if (utente != null) {
                    $(".error").html(``);
                    if (utente.saldo >= offerta && offerta > prezzo) {
                        $('.offer-input button').removeClass('disabled');
                        $(".error").html(``);
                    } else {
                        $('.offer-input button').addClass('disabled');
                        $(".message").html(``);
                        $(".error").html(`<p class="mt-3">Il tuo saldo non é sufficiente oppure hai fatto un'offerta non valida!</p>`);
                    }
                } else {
                    $(".error").html(`<p class="mt-3">Devi effettuare il login!</p>`);
                }

                if (offerta.length === 0) {
                    $(".error").html(``);
                }
            });
        });

        $('.offer-input button').click(function (event) {
            event.preventDefault();

            let params = {
                asta : $('.offer-input input[name = asta]').val(),
                offerta : $('.offer-input input[name = offerta]').val()
            }

            $.post(ctx+'/newOffer', $.param(params), function(response) {
                if(response) {
                    $('.message').html(`<p class="mt-3" style="color: #05cf48 !important;">Offerta registrata correttamente!</p>`);
                    let prezzo = Number(params.offerta).toFixed(2);
                    prezzo = prezzo.toString().replace('.', ',');
                    prezzo = '€ '.concat(prezzo);
                    $('.best-offer h5').html(prezzo);
                } else {
                    $('.message').html(`<p class="mt-3" style="color: #BB371A !important;">Problema con la registrazione dell'offerta</p>`);
                }
            });

        });



    }


});