package com.altimit_server.types;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * Information for of a client
 */
public class ClientInfo {

    /** Client socket **/
    public Socket socket;

    /**
     * Client info constructor.
     * @param socket Socket of the client.
     */
    public ClientInfo(Socket socket) {
        this.socket = socket;
    }
}
