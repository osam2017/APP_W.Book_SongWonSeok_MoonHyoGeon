# easy-request

[![https://img.shields.io/npm/v/easy-post-request.svg](https://img.shields.io/npm/v/easy-post-request.svg)](https://www.npmjs.com/package/easy-post-request)
[![https://img.shields.io/npm/dm/easy-post-request.svg](https://img.shields.io/npm/dm/easy-post-request.svg)](https://www.npmjs.com/package/easy-post-request)

Simple HTTP POST requester.

## Example of use

```
    $ npm i easy-post-request --save
```

After install:

```js

    const post = require('easy-post-request');

    post('http://someurl',{someParameter:'someValue'})
        .then((body) => {
            console.log(body);
        })
        .catch((err) => {
            console.error(err);
        });

```
