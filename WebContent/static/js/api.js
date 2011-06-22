

(function($, name) {
    var api = window[name] = {};
    var get_method = 'searchconf searchexpert'.split(' ');

    $(get_method).each(function(i, e) {
        api[e] = function(param) {
            return $.getJSON('api/' + e, param);
        }
    });
})(jQuery, 'API');

