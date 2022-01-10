
$(document).ready(function() {
    var params = {
        page: "home",
        action: "inCorso",
    };
    var path = "${pageContext.servletContext.contextPath}";
    $.post(path+"/getAuctions", $.param(params), function(response) {
        console.log(response);
    });
});