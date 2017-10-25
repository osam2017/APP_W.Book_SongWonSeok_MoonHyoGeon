const chai = require('chai');
const post = require('../index');
chai.should();

describe("Easy Post Request Index", () => {
    it("#post", (done) => {
        (async () => {
            try {
                let result = await post('https://httpbin.org/post', {});
                result.url.should.be.equal('https://httpbin.org/post');
                done();
            } catch (err) {
                done(err);
            }
        })();
    });
});