(function($) {
    var chart;
    var total_year = 10;
    var end_year = 2011;
    var categories = [];
    for (var i = total_year - 1; i >= 0; i--) {
        categories.push('' + (end_year - total_year));
    }

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
               marginRight: 130,
               marginBottom: 25
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
            API.person({
                q: $('#query_input').val(),
                start_year: end_year - total_year + 1,
                end_year: end_year
            }).success(function(r) {
                create_chart({
                    title: r.name,
                    subtitle: 'personal publication and citation number graph',
                    data: r.data,
                    categories: categories
                });
            });
        });
    };

    $(document).ready(function() {
        attach_event();
    });
    
})(jQuery);
