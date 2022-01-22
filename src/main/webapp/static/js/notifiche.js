

$(document).ready(function() {

    nonLette = $("#non-lette")

    lette = $("#gia-lette")
    $("button.leggi").click(read)

    $("button.non-leggi").click(unread)
    $("button.elimina").click(elimina)
});

function creaNotificaLetta(elemento) {

    let td = elemento.find(".td")
    let button = td.find(".leggi")
    console.log(td)
    let value = button.val()
    console.log(value)

    button.remove()

    let buttonNonLetta = $(document.createElement('button')).prop({
        type: 'button',
        innerHTML: 'segna come non letta',
        class: 'btn non-leggi',
        "value" : value
    })
    td.append(buttonNonLetta)
    buttonNonLetta.click(unread)
    let buttonElimina = $(document.createElement('button')).prop({
        type: 'button',
        innerHTML: 'elimina',
        class: 'btn elimina ml-3',
        "value" : value
    })
    buttonElimina.click(elimina)
    td.append(buttonElimina)

}

function creaNotificaNonLetta(elemento) {

    let td = elemento.find(".td")
    let eliminaButton = td.find(".elimina")
    let unreadButton = td.find(".non-leggi")
    let val = eliminaButton.val();
    eliminaButton.remove()
    unreadButton.remove()


    let buttonLeggi = $(document.createElement('button')).prop({
        type: 'button',
        innerHTML: 'segna come letta',
        class: 'btn leggi',
        "value": val
    })
    td.append(buttonLeggi)
    buttonLeggi.click(read)

}

function read(){
    let idBottone = $(this).val();
    let elem = $(this).parent().parent();
    $.get(ctx + "/readNotification",{"action" : "read", "idNotifica" : idBottone},function (){

        let copia = elem.clone();
        lette.append(copia)
        elem.remove()
        creaNotificaLetta(copia)
    })
}

function unread(){
    let idBottone = $(this).val();
    let elem = $(this).parent().parent();
    $.get(ctx + "/readNotification",{"action" : "unread", "idNotifica" : idBottone},function (){

        let copia = elem.clone();
        nonLette.append(copia)
        elem.remove()
        creaNotificaNonLetta(copia);
    })
}

function elimina(){

    let idBottone = $(this).val();
    let elem = $(this).parent().parent();
    $.get(ctx + "/deleteNotification", {"idNotifica": idBottone}, function () {

        elem.remove()
    })
}