

(function($, name) {
    var api = window[name] = {};
    var get_method = 'person searchconf'.split(' ');

    $(get_method).each(function(i, e) {
        api[e] = function(param) {
            return $.getJSON('api/' + e, param);
        }
    });
})(jQuery, 'API');

