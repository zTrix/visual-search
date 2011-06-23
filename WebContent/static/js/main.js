(function($) {
    var chart;

    var category_array = function(st, ed) {
        var ret = [];
        for (var i = st ; i <= ed; i++) {
            ret.push('' + i);
        }
        return ret;
    };

    var create_chart = function(option) {
        var defaults = {
            title: '',
            subtitle: '',
            data: [],
            categories: []
        };
        $.extend(defaults, option);
        console.log(defaults);
        
        chart = new Highcharts.Chart({
            chart: {
               renderTo: 'graph',
               defaultSeriesType: 'line',
               marginRight: 50,
               marginBottom: 35
            },
            title: {
               text: defaults.title,
               x: -20 
            },
            subtitle: {
               text: defaults.subtitle,
               x: -20
            },
            xAxis: {
               categories: defaults.categories
            },
            yAxis: {
               title: {
                  text: 'Personal hits'
               },
               plotLines: [{
                  value: 0,
                  width: 1,
                  color: '#808080'
               }]
            },
            tooltip: {
               formatter: function() {
                         return '<b>'+ this.series.name +'</b><br/>'+
                     this.x +': '+ this.y +'';
               }
            },
            legend: {
               layout: 'vertical',
               align: 'right',
               verticalAlign: 'top',
               x: -10,
               y: 100,
               borderWidth: 0
            },
            series: defaults.data
       });
    };

    var attach_event = function() {
        $('#search').submit(function(e) {
            e.preventDefault();
            if ($('#query_input').val().length == 0) return;
            var start_year = +($('#start-year').val());
            var end_year = +($('#end-year').val());
            $('#loading').css('opacity', 1);
            if ($('#type').val() == 'people') {
                API.person({
                    q: $('#query_input').val(),
                    start_year: start_year,
                    end_year: end_year
                }).success(function(r) {
                    $('#graph').css('opacity', 1);
                    $('#loading').css('opacity', 0);
                    create_chart({
                        title: r.name,
                        subtitle: 'personal publication and citation number trend',
                        data: r.data,
                        categories: category_array(start_year, end_year)
                    });
                });
            } else if ($('#type').val() == 'publication') {
                var query = $('#query_input').val();
                API.publication({
                    q: query,
                    start_year: start_year,
                    end_year: end_year
                }).success(function(r) {
                    $('#graph').css('opacity', 1);
                    $('#loading').css('opacity', 0);
                    create_chart({
                        title: query,
                        subtitle: 'publication and citation number trend',
                        data: r.data,
                        categories: category_array(start_year, end_year)
                    });
                });
            }
        });

        $('#type').change(function(){
            var s = $('#type');
            //$('#query_input').focus();
            if (s.val() == 'people') {
                $('#query_input').attr('placeholder', 'Search people e.g.: bo wang');
            } else if (s.val() == 'publication') {
                $('#query_input').attr('placeholder', 'Search publication');
            }
        });
    };

    $(document).ready(function() {
        $('#loading').css('opacity', 0);
        attach_event();
        create_chart();
        $('#start-year').val(2002);
        $('#end-year').val(2010);
    });
    
})(jQuery);
