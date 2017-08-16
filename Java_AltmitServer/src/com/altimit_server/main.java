package com.altimit_server;

/**
 * This class handles the listening of clients connections, compiling all AltimitCmd classes, and connecting to hazelcast.
 */
public class main {
    /** Main method of the server **/
    public static void main(String[] args) {
        //Start server
        AltimitNetwork.getInstance().StartAltimitServer();
    }
}
