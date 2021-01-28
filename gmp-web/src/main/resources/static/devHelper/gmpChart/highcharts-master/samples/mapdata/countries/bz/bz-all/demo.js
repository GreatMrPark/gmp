// Prepare demo data
// Data is joined to map using value of 'hc-key' property by default.
// See API docs for 'joinBy' for more info on linking data and map.
var data = [
    ['bz-5784', 0],
    ['bz-bz', 1],
    ['bz-cz', 2],
    ['bz-cy', 3],
    ['bz-ow', 4],
    ['bz-sc', 5],
    ['bz-to', 6]
];

// Create the chart
Highcharts.mapChart('container', {
    chart: {
        map: 'countries/bz/bz-all'
    },

    title: {
        text: 'Highmaps basic demo'
    },

    subtitle: {
        text: 'Source map: <a href="http://code.highcharts.com/mapdata/countries/bz/bz-all.js">Belize</a>'
    },

    mapNavigation: {
        enabled: true,
        buttonOptions: {
            verticalAlign: 'bottom'
        }
    },

    colorAxis: {
        min: 0
    },

    series: [{
        data: data,
        name: 'Random data',
        states: {
            hover: {
                color: '#BADA55'
            }
        },
        dataLabels: {
            enabled: true,
            format: '{point.name}'
        }
    }]
});
