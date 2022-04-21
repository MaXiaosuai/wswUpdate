var exec = require('cordova/exec');

module.exports = {
    wswUpdate:function (arg0, success, error) {
        exec(success, error, 'wswAlipay', 'aliLogin', [arg0]);
    }
}
