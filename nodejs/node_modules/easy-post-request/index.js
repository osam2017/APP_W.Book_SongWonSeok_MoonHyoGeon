const request = require('request');
const Q       = require('q');

module.exports = (url, parameters) => {
    let promise = Q.defer();

    if (!parameters instanceof Object || parameters === null || typeof parameters === 'undefined') {

        promise.reject('Parameters type error');

    } else {

        request({
            method: 'POST',
            url   : url,
            json  : true, // indicates the returning data is JSON, no need for JSON.parse()
            body  : parameters
        }, (err, res, body) => {
            if (err) {
                promise.reject(err);
            } else {
                promise.resolve(body);
            }
        });

    }

    return promise.promise;
};

