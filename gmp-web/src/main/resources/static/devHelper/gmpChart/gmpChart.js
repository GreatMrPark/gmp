/**
 *  (c) 2021-2021 GreatMrPark
 *  License: www.greatmrpark.com/license
 */
'use strict';
(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD를 위한 define이 존재한다면
        // define(['b'], factory);
        
        define('greatmrpark/gmpcharts', function () {
            return factory(root);
        });
    } else if (typeof module === 'object' && module.exports) {
        // Node.js나 CommonJS를 위한 module.exports가 존재한다면
        // module.exports = factory(require('b'));

        factory['default'] = factory;
        module.exports = root.document ? factory(root) : factory;
    } else {
        // 브라우저 window 객체
        // root.returnExports = factory(root.b);

        if (root.Gmpcharts) {
            root.Gmpcharts.error(16, true);
        }
        root.Gmpcharts = factory(root);
    }
}(typeof window !== 'undefined' ? window : this, function (win) {
    console.log("Gmpcharts");
    var _modules = {};
    function _registerModule(obj, path, args, fn) {
//        console.log(obj);
//        console.log(path);
//        console.log(args);
//        console.log(fn);
//        console.log(obj.hasOwnProperty(path));
        if (!obj.hasOwnProperty(path)) {
            obj[path] = fn.apply(null, args);
//            console.log(obj[path]);
        }
        console.log(obj);
    }
    _registerModule(_modules, 'core/Globals.js', [], function () {
        var glob = (
            typeof win !== 'undefined' ?
                win :
                typeof window !== 'undefined' ?
                    window :
                    {}), doc = glob.document, SVG_NS = 'http://www.w3.org/2000/svg', userAgent = (glob.navigator && glob.navigator.userAgent) || '', svg = (doc &&
                doc.createElementNS &&
                !!doc.createElementNS(SVG_NS, 'svg').createSVGRect), isMS = /(edge|msie|trident)/i.test(userAgent) && !glob.opera, isFirefox = userAgent.indexOf('Firefox') !== -1, isChrome = userAgent.indexOf('Chrome') !== -1, hasBidiBug = (isFirefox &&
                parseInt(userAgent.split('Firefox/')[1], 10) < 4 // issue #38
            );
        var G = {
                product: 'Gmpcharts',
                version: '8.2.2',
                deg2rad: Math.PI * 2 / 360,
                doc: doc,
                hasBidiBug: hasBidiBug,
                hasTouch: !!glob.TouchEvent,
                isMS: isMS,
                isWebKit: userAgent.indexOf('AppleWebKit') !== -1,
                isFirefox: isFirefox,
                isChrome: isChrome,
                isSafari: !isChrome && userAgent.indexOf('Safari') !== -1,
                isTouchDevice: /(Mobile|Android|Windows Phone)/.test(userAgent),
                SVG_NS: SVG_NS,
                chartCount: 0,
                seriesTypes: {},
                symbolSizes: {},
                svg: svg,
                win: glob,
                marginNames: ['plotTop', 'marginRight', 'marginBottom', 'plotLeft'],
                noop: function () { },
                charts: [],
                dateFormats: {}
            };

        return G;
    });
}));
