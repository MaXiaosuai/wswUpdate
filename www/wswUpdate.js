var exec = require('cordova/exec');

module.exports = {
    wswUpdate:function (arg0, success, error) {
        exec(success, error, 'wswUpdate', 'wswUpdate', [arg0]);
    }
}
