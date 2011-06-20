

(function($) {
    for (var e in API) {
        $('<option/>').attr('value', e)
                      .text(e)
                      .appendTo('#action');
    }
    $('#submit').click(function() {
        var action = $('#action option:selected').val();
        var v = eval('({' + $('#values').val() + '})');
        API[action](v).success(function(d) {
            $('#result').text(JSON.stringify(d, null, '    '));
        });
    });
})(jQuery);
