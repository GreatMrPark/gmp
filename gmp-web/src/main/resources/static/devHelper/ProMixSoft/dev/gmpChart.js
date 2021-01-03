var barChart = bb.generate({
  data: {
    columns: [
    ["CPU", 30, 200, 100, 400, 150, 250],
    ["RAM", 130, 100, 140, 200, 150, 50]
    ],
    type: "bar", // for ESM specify as: bar()
  },
  bar: {
    width: {
      ratio: 0.5
    }
  },
  bindto: "#barChart"
});

setTimeout(function() {
    barChart.load({
        columns: [
            ["DISK", 130, -150, 200, 300, -200, 100]
        ]
    });
}, 1000);

var lineChart = bb.generate({
    data: {
      columns: [
      ["CPU", 30, 200, 100, 400, 150, 250],
      ["RAM", 50, 20, 10, 40, 15, 25]
      ],
      type: "line", // for ESM specify as: line()
    },
    bindto: "#lineChart"
  });

  setTimeout(function() {
      lineChart.load({
          columns: [
              ["CPU", 230, 190, 300, 500, 300, 400]
          ]
      });
  }, 1000);

  setTimeout(function() {
      lineChart.load({
          columns: [
              ["DISK", 130, 150, 200, 300, 200, 100]
          ]
      });
  }, 1500);

  setTimeout(function() {
      lineChart.unload({
          ids: "CPU"
      });
  }, 2000);
  
var gaugeChart = bb.generate({
    data: {
        columns: [
            ["CPU", 91.4]
        ],
        type: "gauge", // for ESM specify as: gauge()
        onclick: function(d, i) {
            console.log("onclick", d, i);
        },
        onover: function(d, i) {
            console.log("onover", d, i);
        },
        onout: function(d, i) {
            console.log("onout", d, i);
        }
    },
    gauge: {},
    color: {
      pattern: [
        "#FF0000",
        "#F97600",
        "#F6C600",
        "#60B044"
      ],
      threshold: {
        values: [
          30,
          60,
          90,
          100
        ]
      }
    },
    size: {
      height: 180
    },
    bindto: "#gaugeChart"
  });

  setTimeout(function() {
      gaugeChart.load({
          columns: [["CPU", 10]]
      });
  }, 1000);

  setTimeout(function() {
      gaugeChart.load({
          columns: [["CPU", 50]]
      });
  }, 2000);

  setTimeout(function() {
      gaugeChart.load({
          columns: [["CPU", 70]]
      });
  }, 3000);

  setTimeout(function() {
      gaugeChart.load({
          columns: [["CPU", 0]]
      });
  }, 4000);

  setTimeout(function() {
      gaugeChart.load({
          columns: [["CPU", 100]]
      });
  }, 5000);