/*
 Highcharts JS v8.2.2 (2020-10-22)

 (c) 2009-2019 Torstein Honsi

 License: www.highcharts.com/license
*/
(function(m) {
    "object" === typeof module && module.exports ? (m["default"] = m, module.exports = m) : "function" === typeof define && define.amd ? define("highcharts/modules/series-label", ["highcharts"], function(u) {
        m(u);
        m.Highcharts = u;
        return m
    }) : m("undefined" !== typeof Highcharts ? Highcharts : void 0)
})(function(m) {
    function u(m, z, y, u) {
        m.hasOwnProperty(z) || (m[z] = u.apply(null, y))
    }
    m = m ? m._modules : {};
    u(m, "Extensions/SeriesLabel.js", [m["Core/Animation/AnimationUtilities.js"], m["Core/Chart/Chart.js"], m["Core/Globals.js"], m["Core/Renderer/SVG/SVGRenderer.js"],
        m["Core/Utilities.js"]
    ], function(m, u, y, H, w) {
        function B(e, d, a, l, f, g) {
            e = (g - d) * (a - e) - (l - d) * (f - e);
            return 0 < e ? !0 : !(0 > e)
        }

        function C(e, d, a, l, f, g, b, k) {
            return B(e, d, f, g, b, k) !== B(a, l, f, g, b, k) && B(e, d, a, l, f, g) !== B(e, d, a, l, b, k)
        }

        function z(e, d, a, l, f, g, b, k) {
            return C(e, d, e + a, d, f, g, b, k) || C(e + a, d, e + a, d + l, f, g, b, k) || C(e, d + l, e + a, d + l, f, g, b, k) || C(e, d, e, d + l, f, g, b, k)
        }

        function F(e) {
            if (this.renderer) {
                var d = this,
                    a = D(d.renderer.globalAnimation).duration;
                d.labelSeries = [];
                d.labelSeriesMaxSum = 0;
                w.clearTimeout(d.seriesLabelTimer);
                d.series.forEach(function(l) {
                    var f = l.options.label,
                        g = l.labelBySeries,
                        b = g && g.closest;
                    f.enabled && l.visible && (l.graph || l.area) && !l.isSeriesBoosting && (d.labelSeries.push(l), f.minFontSize && f.maxFontSize && (l.sum = l.yData.reduce(function(a, b) {
                        return (a || 0) + (b || 0)
                    }, 0), d.labelSeriesMaxSum = Math.max(d.labelSeriesMaxSum, l.sum)), "load" === e.type && (a = Math.max(a, D(l.options.animation).duration)), b && ("undefined" !== typeof b[0].plotX ? g.animate({
                        x: b[0].plotX + b[1],
                        y: b[0].plotY + b[2]
                    }) : g.attr({
                        opacity: 0
                    })))
                });
                d.seriesLabelTimer =
                    I(function() {
                        d.series && d.labelSeries && d.drawSeriesLabels()
                    }, d.renderer.forExport || !a ? 0 : a)
            }
        }
        var D = m.animObject;
        m = w.addEvent;
        var G = w.extend,
            J = w.fireEvent,
            K = w.format,
            E = w.isNumber,
            A = w.pick,
            L = w.setOptions,
            I = w.syncTimeout;
        "";
        y = y.Series;
        L({
            plotOptions: {
                series: {
                    label: {
                        enabled: !0,
                        connectorAllowed: !1,
                        connectorNeighbourDistance: 24,
                        format: void 0,
                        formatter: void 0,
                        minFontSize: null,
                        maxFontSize: null,
                        onArea: null,
                        style: {
                            fontWeight: "bold"
                        },
                        boxesToAvoid: []
                    }
                }
            }
        });
        H.prototype.symbols.connector = function(e, d, a, l, f) {
            var g =
                f && f.anchorX;
            f = f && f.anchorY;
            var b = a / 2;
            if (E(g) && E(f)) {
                var k = [
                    ["M", g, f]
                ];
                var h = d - f;
                0 > h && (h = -l - h);
                h < a && (b = g < e + a / 2 ? h : a - h);
                f > d + l ? k.push(["L", e + b, d + l]) : f < d ? k.push(["L", e + b, d]) : g < e ? k.push(["L", e, d + l / 2]) : g > e + a && k.push(["L", e + a, d + l / 2])
            }
            return k || []
        };
        y.prototype.getPointsOnGraph = function() {
            function e(b) {
                var c = Math.round(b.plotX / 8) + "," + Math.round(b.plotY / 8);
                n[c] || (n[c] = 1, a.push(b))
            }
            if (this.xAxis || this.yAxis) {
                var d = this.points,
                    a = [],
                    l;
                var f = this.graph || this.area;
                var g = f.element;
                var b = this.chart.inverted,
                    k = this.xAxis;
                var h = this.yAxis;
                var m = b ? h.pos : k.pos;
                b = b ? k.pos : h.pos;
                k = A(this.options.label.onArea, !!this.area);
                var x = h.getThreshold(this.options.threshold),
                    n = {};
                if (this.getPointSpline && g.getPointAtLength && !k && d.length < this.chart.plotSizeX / 16) {
                    if (f.toD) {
                        var c = f.attr("d");
                        f.attr({
                            d: f.toD
                        })
                    }
                    var v = g.getTotalLength();
                    for (l = 0; l < v; l += 16) h = g.getPointAtLength(l), e({
                        chartX: m + h.x,
                        chartY: b + h.y,
                        plotX: h.x,
                        plotY: h.y
                    });
                    c && f.attr({
                        d: c
                    });
                    h = d[d.length - 1];
                    h.chartX = m + h.plotX;
                    h.chartY = b + h.plotY;
                    e(h)
                } else
                    for (v = d.length, l = 0; l < v; l += 1) {
                        h =
                            d[l];
                        c = d[l - 1];
                        h.chartX = m + h.plotX;
                        h.chartY = b + h.plotY;
                        k && (h.chartCenterY = b + (h.plotY + A(h.yBottom, x)) / 2);
                        if (0 < l && (f = Math.abs(h.chartX - c.chartX), g = Math.abs(h.chartY - c.chartY), f = Math.max(f, g), 16 < f))
                            for (f = Math.ceil(f / 16), g = 1; g < f; g += 1) e({
                                chartX: c.chartX + g / f * (h.chartX - c.chartX),
                                chartY: c.chartY + g / f * (h.chartY - c.chartY),
                                chartCenterY: c.chartCenterY + g / f * (h.chartCenterY - c.chartCenterY),
                                plotX: c.plotX + g / f * (h.plotX - c.plotX),
                                plotY: c.plotY + g / f * (h.plotY - c.plotY)
                            });
                        E(h.plotY) && e(h)
                    }
                return a
            }
        };
        y.prototype.labelFontSize =
            function(e, d) {
                return e + this.sum / this.chart.labelSeriesMaxSum * (d - e) + "px"
            };
        y.prototype.checkClearPoint = function(e, d, a, l) {
            var f = Number.MAX_VALUE,
                g = Number.MAX_VALUE,
                b, k = A(this.options.label.onArea, !!this.area),
                h = k || this.options.label.connectorAllowed,
                m = this.chart,
                x;
            for (x = 0; x < m.boxesToAvoid.length; x += 1) {
                var n = m.boxesToAvoid[x];
                var c = e + a.width;
                var v = d;
                var q = d + a.height;
                if (!(e > n.right || c < n.left || v > n.bottom || q < n.top)) return !1
            }
            for (x = 0; x < m.series.length; x += 1)
                if (v = m.series[x], n = v.interpolatedPoints, v.visible &&
                    n) {
                    for (c = 1; c < n.length; c += 1) {
                        if (n[c].chartX >= e - 16 && n[c - 1].chartX <= e + a.width + 16) {
                            if (z(e, d, a.width, a.height, n[c - 1].chartX, n[c - 1].chartY, n[c].chartX, n[c].chartY)) return !1;
                            this === v && !b && l && (b = z(e - 16, d - 16, a.width + 32, a.height + 32, n[c - 1].chartX, n[c - 1].chartY, n[c].chartX, n[c].chartY))
                        }
                        if ((h || b) && (this !== v || k)) {
                            q = e + a.width / 2 - n[c].chartX;
                            var u = d + a.height / 2 - n[c].chartY;
                            f = Math.min(f, q * q + u * u)
                        }
                    }
                    if (!k && h && this === v && (l && !b || f < Math.pow(this.options.label.connectorNeighbourDistance, 2))) {
                        for (c = 1; c < n.length; c += 1)
                            if (b =
                                Math.min(Math.pow(e + a.width / 2 - n[c].chartX, 2) + Math.pow(d + a.height / 2 - n[c].chartY, 2), Math.pow(e - n[c].chartX, 2) + Math.pow(d - n[c].chartY, 2), Math.pow(e + a.width - n[c].chartX, 2) + Math.pow(d - n[c].chartY, 2), Math.pow(e + a.width - n[c].chartX, 2) + Math.pow(d + a.height - n[c].chartY, 2), Math.pow(e - n[c].chartX, 2) + Math.pow(d + a.height - n[c].chartY, 2)), b < g) {
                                g = b;
                                var p = n[c]
                            } b = !0
                    }
                } return !l || b ? {
                x: e,
                y: d,
                weight: f - (p ? g : 0),
                connectorPoint: p
            } : !1
        };
        u.prototype.drawSeriesLabels = function() {
            var e = this,
                d = this.labelSeries;
            e.boxesToAvoid = [];
            d.forEach(function(a) {
                a.interpolatedPoints =
                    a.getPointsOnGraph();
                (a.options.label.boxesToAvoid || []).forEach(function(a) {
                    e.boxesToAvoid.push(a)
                })
            });
            e.series.forEach(function(a) {
                function d(a, b, c) {
                    var d = Math.max(h, A(y, -Infinity)),
                        e = Math.min(h + u, A(z, Infinity));
                    return a > d && a <= e - c.width && b >= m && b <= m + n - c.height
                }
                var f = a.options.label;
                if (f && (a.xAxis || a.yAxis)) {
                    var g = [],
                        b, k, h = (k = e.inverted) ? a.yAxis.pos : a.xAxis.pos,
                        m = k ? a.xAxis.pos : a.yAxis.pos,
                        u = e.inverted ? a.yAxis.len : a.xAxis.len,
                        n = e.inverted ? a.xAxis.len : a.yAxis.len,
                        c = a.interpolatedPoints,
                        v = A(f.onArea,
                            !!a.area),
                        q = a.labelBySeries,
                        w = !q;
                    var p = f.minFontSize;
                    var r = f.maxFontSize;
                    var t = "highcharts-color-" + A(a.colorIndex, "none");
                    if (v && !k) {
                        k = [a.xAxis.toPixels(a.xData[0]), a.xAxis.toPixels(a.xData[a.xData.length - 1])];
                        var y = Math.min.apply(Math, k);
                        var z = Math.max.apply(Math, k)
                    }
                    if (a.visible && !a.isSeriesBoosting && c) {
                        q || (q = a.name, "string" === typeof f.format ? q = K(f.format, a, e) : f.formatter && (q = f.formatter.call(a)), a.labelBySeries = q = e.renderer.label(q, 0, -9999, "connector").addClass("highcharts-series-label highcharts-series-label-" +
                            a.index + " " + (a.options.className || "") + " " + t), e.renderer.styledMode || (q.css(G({
                            color: v ? e.renderer.getContrast(a.color) : a.color
                        }, f.style || {})), q.attr({
                            opacity: e.renderer.forExport ? 1 : 0,
                            stroke: a.color,
                            "stroke-width": 1
                        })), p && r && q.css({
                            fontSize: a.labelFontSize(p, r)
                        }), q.attr({
                            padding: 0,
                            zIndex: 3
                        }).add());
                        p = q.getBBox();
                        p.width = Math.round(p.width);
                        for (k = c.length - 1; 0 < k; --k) v ? (r = c[k].chartX - p.width / 2, t = c[k].chartCenterY - p.height / 2, d(r, t, p) && (b = a.checkClearPoint(r, t, p))) : (r = c[k].chartX + 3, t = c[k].chartY - p.height -
                            3, d(r, t, p) && (b = a.checkClearPoint(r, t, p, !0)), b && g.push(b), r = c[k].chartX + 3, t = c[k].chartY + 3, d(r, t, p) && (b = a.checkClearPoint(r, t, p, !0)), b && g.push(b), r = c[k].chartX - p.width - 3, t = c[k].chartY + 3, d(r, t, p) && (b = a.checkClearPoint(r, t, p, !0)), b && g.push(b), r = c[k].chartX - p.width - 3, t = c[k].chartY - p.height - 3, d(r, t, p) && (b = a.checkClearPoint(r, t, p, !0))), b && g.push(b);
                        if (f.connectorAllowed && !g.length && !v)
                            for (r = h + u - p.width; r >= h; r -= 16)
                                for (t = m; t < m + n - p.height; t += 16)(b = a.checkClearPoint(r, t, p, !0)) && g.push(b);
                        if (g.length) {
                            if (g.sort(function(a,
                                    b) {
                                    return b.weight - a.weight
                                }), b = g[0], e.boxesToAvoid.push({
                                    left: b.x,
                                    right: b.x + p.width,
                                    top: b.y,
                                    bottom: b.y + p.height
                                }), (c = Math.sqrt(Math.pow(Math.abs(b.x - (q.x || 0)), 2) + Math.pow(Math.abs(b.y - (q.y || 0)), 2))) && a.labelBySeries && (g = {
                                    opacity: e.renderer.forExport ? 1 : 0,
                                    x: b.x,
                                    y: b.y
                                }, f = {
                                    opacity: 1
                                }, 10 >= c && (f = {
                                    x: g.x,
                                    y: g.y
                                }, g = {}), c = void 0, w && (c = D(a.options.animation), c.duration *= .2), a.labelBySeries.attr(G(g, {
                                    anchorX: b.connectorPoint && b.connectorPoint.plotX + h,
                                    anchorY: b.connectorPoint && b.connectorPoint.plotY + m
                                })).animate(f,
                                    c), a.options.kdNow = !0, a.buildKDTree(), a = a.searchPoint({
                                    chartX: b.x,
                                    chartY: b.y
                                }, !0))) q.closest = [a, b.x - (a.plotX || 0), b.y - (a.plotY || 0)]
                        } else q && (a.labelBySeries = q.destroy())
                    } else q && (a.labelBySeries = q.destroy())
                }
            });
            J(e, "afterDrawSeriesLabels")
        };
        m(u, "load", F);
        m(u, "redraw", F)
    });
    u(m, "masters/modules/series-label.src.js", [], function() {})
});
//# sourceMappingURL=series-label.js.map