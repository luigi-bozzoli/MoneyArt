function isEmpty(str) {
    return (!str || str.length === 0);
}
function b64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    let byteCharacters = atob(b64Data);
    let byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        let slice = byteCharacters.slice(offset, offset + sliceSize);

        let byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        let byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    let blob = new Blob(byteArrays, { type: contentType });
    return blob;
}
function picClick() {
    $('#picCropped').click();
}

function stateChanged() {
    let reader = new FileReader();
    reader.onload = function (e) {
        $uploadCrop.croppie('bind', {
            url: e.target.result
        }).then(function(){
            console.log('jQuery bind complete');
        });

    }
    let pic = $('#picCropped').get(0);
    reader.readAsDataURL(pic.files[0]);
    $('.image-crop-wrap').removeClass('d-none');
    $('.pic').removeClass('d-flex').addClass('d-none');
}

$(document).ready(function() {

    if ($(window).width() < 769) {
        $uploadCrop = $('#upload-input').croppie({
            enableExif: true,
            viewport: {
                width: 200,
                height: 200,
                type: 'square'
            },
            boundary: {
                width: 300,
                height: 300
            }
        });
    }
    else {
        $uploadCrop = $('#upload-input').croppie({
            enableExif: true,
            viewport: {
                width: 500,
                height: 500,
                type: 'square'
            },
            boundary: {
                width: 600,
                height: 600
            }
        });
    }


    $('#picCropped').on('change', function() {
        stateChanged();
    });

    $('#crop-result').on('click', function (ev) {
        $uploadCrop.croppie('result', {
            type: 'base64',
            size: 'viewport'
        }).then(function (resp) {
            
            let ImageURL = resp;
            let block = ImageURL.split(";");
            // Get the content type of the image
            let contentType = block[0].split(":")[1];
            // get the real base64 content of the file
            let realData = block[1].split(",")[1];

            // Convert it to a blob to upload
            let blob = b64toBlob(realData, contentType);

            $('.pic img').attr('src',
                window.URL.createObjectURL(blob));
            $('.pic').removeClass('d-none').addClass('d-flex');
            $('.image-crop-wrap').addClass('d-none');
            $('#picForm').val(resp);
        });
    });

    $('#cancel-crop').click(function () {
        $('.pic').removeClass('d-none').addClass('d-flex');
        $('.image-crop-wrap').addClass('d-none');
        $('.pic img').attr('src',
            ctx + '/static/image/noimage.jpg');
        $('#picForm').val('');
        $('#picCropped').val('');
    })

    $('#create').click(function () {
        if(isEmpty($('#picForm').val())) {
            $(".error").html(`<p class="text-center">Inserisci l'immagine dell'opera!<i class="fas fa-exclamation-triangle ml-2"></i></p>`);
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        } else if (isEmpty($('.data input[name = name]').val()) || isEmpty($('.data textarea[name = description]').val())) {
            $(".error").html('<p class="text-center">Il titolo e la descrizione non possono essere vuoti!<i class="fas fa-exclamation-triangle ml-2"></i></p>');
            $("html, body").animate({scrollTop: 0}, 500);
            $(".error").delay(550).effect("shake");
            return false;
        }
    });
})