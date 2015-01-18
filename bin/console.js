#!/usr/bin/env node

var readline = require('readline'),
    rl = readline.createInterface(process.stdin, process.stdout);
var gremlin = require('gremlin-client')
var client  = gremlin.createClient({language:'Clojure'})

rl.setPrompt('mogwai> ');
rl.prompt();

rl.on('line', function(line) {
  if (line != "") {
    client.execute(line, {}, function(err, result) {
      console.log('returned');
      if (err) {
        console.log(err);
      }
      else {
        console.dir(result);
      }
      rl.prompt();
    });
  }
  else {
    rl.prompt();
  }
}).on('close', function() {
  console.log('Have a great day!');
  process.exit(0);
});
