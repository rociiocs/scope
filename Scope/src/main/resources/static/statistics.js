// Using Node.js
const https = require('https');
const crypto = require('crypto');
var hostname = 'api.cloud.wowza.com'
var path = '/api/v1.6/usage/stream_targets/summary';
//For security, never reveal API key in client-side code
var wscApiKey = 'dx9VvMrfjxOxwODCgAJa3xCfzCZqwbTQXRp1c5QlH7m77UYd3ZiWmyFnYupH360a';
var wscAccessKey = 'pfWp5O1BaPASQfe5tUZaXAqdE45tWjAsKWMiz7Z20QrX5EjWnQ2cFIoUJTQ1320d';
var timestamp = Math.round(new Date().getTime()/1000);
var hmacData = (timestamp+':'+path+':'+wscApiKey);
var signature = crypto.createHmac('sha256',wscApiKey).update(hmacData).digest('hex')
const options = {
  hostname: hostname,
  path: path,
  headers: {
    'wsc-access-key': wscAccessKey,
    'wsc-timestamp': timestamp,
    'wsc-signature': signature,
    'Content-Type': 'application/json'
  }
};
https.get(options, function(res) {
  var body = '';
  res.on('data', function(data){
    body += data;
  });
  res.on('end', function() {
    console.log(JSON.parse(body));
  });
}).on('error', function(e) {
  console.log(e.message);
});