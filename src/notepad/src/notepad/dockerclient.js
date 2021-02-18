
// https://www.melvinvivas.com/sample-node-js-application-querying-docker-via-unix-socket/
// https://stackoverflow.com/questions/41177350/node-js-send-get-request-via-unix-socket

const http = require('http');

const options = {
  socketPath: '/var/run/docker.sock',
  path: '/images/json'
};

const callback = resp => {
  console.log(`STATUS: ${resp.statusCode}`);

	var body = '';

    resp.on('data', (chunk) => {
	  body += chunk;
    }).on('end', () => {
        var respArr = JSON.parse(body + '');

        respArr.forEach((value) => {
          console.log(value)  
          // console.log(value.Spec.Name + ' replicas=' + value.Spec.Mode.Replicated.Replicas);

        })

    }).on('error', data => console.error(data));

}

const clientRequest = http.get(options, callback);
clientRequest.end(0);