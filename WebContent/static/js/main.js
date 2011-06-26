(function($) {
    var chart;

    var category_array = function(st, ed) {
        var ret = [];
        for (var i = st ; i <= ed; i++) {
            ret.push('' + i);
        }
        return ret;
    };

    var create_chart_x = function(option) {
        var defaults = {
            title: '',
            subtitle: '',
            data: [],
            categories: []
        };
        $.extend(defaults, option);

        char = new Highcharts.Chart({
            chart: {
                renderTo: 'graph',
                defaultSeriesType: 'column',
                marginRight: 50,
                marginBottom: 70 
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
                categories:  defaults.categories,
                labels: {
                    rotation: -45,
                    align: 'right'
                }
            },
            yAxis: {
                min: 0,
                title: {text: 'Number of papers'}
            },
            tooltip: {
                formatter: function() {
                         return '<b>'+ this.series.name +'</b><br/>'+
                     this.x +': '+ this.y +'';
               }
            },
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'top',
                x: -10,
                y: 100,
                borderWidth: 0
            },
            series:  [{
                name: 'Conference',
                data: defaults.data,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#ffffff',//Highcharts.theme.dataLabelsColor || '#FFFFFF',
                    align: 'left',
                    x: 8,
                    y: -5,
                    formatter: function() {
                       return this.y;
                    },
                    style: {
                       font: 'normal 13px Verdana, sans-serif'
                    }
                }
            }]
        })
    };

    var create_chart = function(option) {
        var defaults = {
            title: '',
            subtitle: '',
            data: [],
            categories: []
        };
        $.extend(defaults, option);
        
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
            } else if ($('#type').val() == 'contribution') {
                var query = $('#query_input').val();
                API.conf({
                    q: query
                }).success(function(r) {
                    $('#graph').css('opacity',1);
                    $('#loading').css('opacity',0);
                    create_chart_x({
                        title: query,
                        subtitle: 'publication distribution for a given topic',
                        data: r.data,
                        categories: r.confname
                    })
                })
            }
        });

        $('#type').change(function(){
            var s = $('#type');
            //$('#query_input').focus();
            if (s.val() == 'people') {
                $('#query_input').attr('placeholder', 'Search people e.g.: bo wang');
            } else if (s.val() == 'publication') {
                $('#query_input').attr('placeholder', 'Search publication');
            } else if (s.val() == 'contribution') {
                $('#query_input').attr('placeholder', 'Search topic');
            }
        });

        $('#edge-editor input').keydown(function(e) {
            if (e.which == 38) {    // up
                $(this).val(+$(this).val() + 1);
            } else if (e.which == 40) { // down
                $(this).val(+$(this).val() - 1);
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
