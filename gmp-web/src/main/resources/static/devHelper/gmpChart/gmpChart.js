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
        if (!obj.hasOwnProperty(path)) {
            obj[path] = fn.apply(null, args);
        }
    }
}));
