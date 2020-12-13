$(function () {
    var counter = 0;
    var $iframe = $('iframe'); // jQuery iframe
    var iframe = $('iframe')[0]; // javascript iframe

    // Requires --disable-web-security to work properly
    $iframe.load(function () {
        $('#iframeStatus').html('Ready').addClass('ready');
        $('#href').html(iframe.contentWindow.location.href);
        $('#href').attr("href", iframe.contentWindow.location.href);
        $('#host').html(iframe.contentWindow.location.host);
        $('#pathname').html(iframe.contentWindow.location.pathname);
        $('#protocol').html(iframe.contentWindow.location.protocol)
        $('#pagesLoaded').html(++counter);
        
        console.log("iFrame Loaded");
        console.log("Iframe's Window Object:");
        console.log(iframe.contentWindow);
        console.log("Iframe's Document:");
        console.log(iframe.contentDocument);
    });

    $("#visit").click(function () {
        $('#iframeStatus').html('Loading').removeClass('ready');
        $('h3').html('Loading url ' + $("#url").val());
        $iframe.attr('src', $("#url").val());
    });
});