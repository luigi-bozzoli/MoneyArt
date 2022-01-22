$(document).ready(function() {
    $('.dropdown-item').click(function () {
        let text = $(this).find('p').text();
        let icon = $(this).find('i').attr('class')

        $('.sort-text').html(text);
        $('.sort-icon').attr('class', icon + ' sort-icon');
        $('.close-drop').removeClass('invisible');

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

        $.get(ctx + '/getResells', $.param(parameters), function(response) {
            $('#container-rivendite').html('');
            $.each(response, function (index, rivendita) {
                let prezzo;
                prezzo = rivendita.prezzo.toFixed(2);
                prezzo = prezzo.toString().replace('.', ',');
                prezzo = '€ '.concat(prezzo);


                $('body').addClass("loading");
                setTimeout(function() {
                    $('body').removeClass("loading");
                }, 200);

                $('#container-rivendite').append(
                    `<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">` +
                        `<div class="thumb-wrapper">` +
                            `<div class="img-box">` +
                                `<img src="` + ctx + `/artworkPicture?id=` + rivendita.opera.id + `"`  +
                                `class="img-responsive">` +
                            `</div>` +
                            `<div class="thumb-content">` +
                                `<h4>` + rivendita.opera.nome +`</h4>` +
                                `<h6>Di ` + rivendita.opera.artista.username + `</h6>` +
                                `<p class="item-price">` + prezzo + `</p>` +
                                `<a href="`+ ctx +`/getResell?id=` + rivendita.id +`" class="btn btn-primary">Acquista</a>` +
                            `</div>` +
                        `</div>` +
                    `</div>`
                );
            });
        });
    });

    $('.close-drop').click(function (){
        $('.sort-text').html('');
        $('.sort-icon').attr('class', 'fas fa-sort');
        $('.close-drop').addClass('invisible');

        $.get(ctx + '/getResells', $.param({action : 'inCorso'}), function(response) {
            $('#container-rivendite').html('');
            $.each(response, function (index, rivendita) {
                let prezzo;
                prezzo = rivendita.prezzo.toFixed(2);
                prezzo = prezzo.toString().replace('.', ',');
                prezzo = '€ '.concat(prezzo);

                $('body').addClass("loading");
                setTimeout(function() {
                    $('body').removeClass("loading");
                }, 200);

                $('#container-rivendite').append(
                    `<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">` +
                    `<div class="thumb-wrapper">` +
                    `<div class="img-box">` +
                    `<img src="` + ctx + `/artworkPicture?id=` + rivendita.opera.id + `"`  +
                    `class="img-responsive">` +
                    `</div>` +
                    `<div class="thumb-content">` +
                    `<h4>` + rivendita.opera.nome +`</h4>` +
                    `<h6>Di ` + rivendita.opera.artista.username + `</h6>` +
                    `<p class="item-price">` + prezzo + `</p>` +
                    `<a href="`+ ctx +`/getResell?id=` + rivendita.id +`" class="btn btn-primary">Acquista</a>` +
                    `</div>` +
                    `</div>` +
                    `</div>`
                );
            });
        });
    });
});