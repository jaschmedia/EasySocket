/**
 * The clientside implementation of EasySocket
 * @author jasch
 * @version 0.1.0
 * @since 0.1.0
 */

(function (window) {
    'use strict'; // why ever, the tutorial says to do it...
    function define_library() {
        /**
         * The library
         * @author jasch
         * @version 0.1.0
         * @since 0.1.0
         */
        var EasySocket = {};

        // Important values for the library
        EasySocket.protocolName = ""; // contains the protocol identifier.
        EasySocket.address = "ws://localhost:8000/ws"; // contains the address to connect to.
        EasySocket.CIS = ""; // connection ID. NAMED INCORRECTLY BUT SCREW IT!
        EasySocket.connectionExists = false;
        EasySocket.events = []; // store all events as {"id": "idstring", "handle": handle}

        /**
         * Creates the WebSocket connection.
         * @author jasch
         * @version 0.1.0
         * @since 0.1.0
         */
        EasySocket.connect = function () {
            EasySocket.ws = new WebSocket(EasySocket.address);
            EasySocket.ws.onmessage = EasySocket.handleMessages;
            EasySocket.ws.onclose = function () {
                console.log("Connection closed.");
            };
            EasySocket.connectionExists = true;
        };

        /**
         * Handles the connection ID stuff.
         * @param event Recieved message.
         */
        EasySocket.handleCIS = function (event) {
            EasySocket.CIS = event.data.substring(3,11);
            EasySocket.ws.send("CIS" + EasySocket.CIS);
        };

        /**
         * Checks the connection ID of the received message. Terminates connection if not.
         * @param event The recieved message.
         * @returns {boolean} Whether the ID is correct.
         */
        EasySocket.checkCID = function (event) {
            if (EasySocket.CIS === event.data.substring(3,11)) {
                return true;
            } else {
                EasySocket.sendError("connectionidmismatch");
                EasySocket.ws.close();
                EasySocket.connectionExists = false;
                return false;
            }
        };

        /**
         * Sends an error message.
         * @param msg The error.
         */
        EasySocket.sendError = function (msg) {
            EasySocket.ws.send("ERR" + EasySocket.CIS + msg);
        };

        EasySocket.handlePRT = function (event) {
            if (EasySocket.checkCID(event)) {
                var serverProtocol = event.data.substring(11,event.data.length);
                if (serverProtocol === EasySocket.protocolName) {
                    EasySocket.ws.send("PRT" + EasySocket.CIS + EasySocket.protocolName);
                } else {
                    EasySocket.sendError("protocolmismatch");
                    EasySocket.ws.close();
                    EasySocket.connectionExists = false;
                }
            }
        };

        EasySocket.handleMessages = function (event) {
            var mtype = event.data.substring(0,3);
            switch (mtype) {
                case "CIS":
                    console.log(event.data);
                    EasySocket.handleCIS(event);
                    break;
                case "PRT":
                    console.log(event.data);
                    EasySocket.handlePRT(event);
                    break;
                case "ERR":
                    console.log(event.data);
                    EasySocket.ws.close();
                    EasySocket.connectionExists = false;
                    break;
                case "EVT":
                    var eventString = event.data.substring(11, event.data.length);
                    var eventID = eventString.split(":")[0];
                    var eventData = eventString.substring(eventID.length + 1, eventString.length);
                    for (var i = 0; i < EasySocket.events.length; i++) {
                        if (EasySocket.events[i].id === eventID) {
                            EasySocket.events[i].handle(eventData);
                        }
                    }
                    break;
                default:
                    break;
            }
        };

        /**
         * Adds a new event listener to the handler
         * @param idstring ID for the event
         * @param handleFunction Function to call for this event.
         */
        EasySocket.registerEvent = function (idstring, handleFunction) {
            var alreadyExists = false;
            for (var i = 0; i < EasySocket.events.length; i++) {
                alreadyExists = (idstring === EasySocket.events[i].id) || alreadyExists;
            }

            if (!alreadyExists) {
                EasySocket.events.push({
                    "id": idstring,
                    "handle": handleFunction,
                })
            }
        };

        /**
         * Remove a registered event.
         * @param idstring The ID of the event.
         */
        EasySocket.removeEvent = function (idstring) {
            for (var i = 0; i < EasySocket.events.length; i++) {
                if (EasySocket.events[i].id === idstring) {
                    EasySocket.events.splice(i, 1);
                }
            }
        };




        return EasySocket;
    }

    // now define the library globally if it doesn't exist yet
    if(typeof (EasySocket) === 'undefined') {
        window.EasySocket = define_library();
    } else {
        console.log("EasySocket was already defined.");
    }
})(window);
