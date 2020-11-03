This repo contains the clojure project and react code file for a test app I made

The clojure project sets up a local http server on localhost:8080 that has one GET endpoint at "/square" that takes a single parameter (a number), and returns the square of that number
It returns a json object like the following:
{
	"input": "7",
	"output": "49"
}
The project SHOULD just run with a "lein run" while sitting in the repo folder...

The react code can then be plugged into https://jscomplete.com/playground to generate a web interface from the react code, that allows you to type a number in, send off a request to the clojure server, read the response, and update the list of responses in realtime


This project doesn't do anything revolutionary, but it's react and clojure talking to each other!!! *insert excitement here*