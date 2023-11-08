
/*const express = require('express');
const path = require('path')
const port = 10090; //nas port
const app = express();

app.use(express.static('app-debug.apk')).get('*', function(req, res){
    res.sendFile('app-debug.apk/index.html');
}).listen(port);
*/

const express = require('express');
const path = require('path')
const port = 10095;
const app = express()
const cors = require('cors');

app.use(express.static('./'))
app.use(cors())
app.set('view engine', 'pug');

app.get('/', (req, res) => {
    res.sendFile('./index.html',{root:__dirname})
});

app.get('/', getRoot);
app.get('/*', getUndefined);

app.listen(port, () => {
    console.log("Server is listening on port "+port);
});

function getRoot(request, response) {
   response.sendFile(path.resolve('./index.html'));
}

function getUndefined(request, response) {
   response.sendFile(path.resolve('./index.html'));
}