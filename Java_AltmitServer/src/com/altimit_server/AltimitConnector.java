package com.altimit_server;

import com.altimit_server.types.ClientInfo;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.UUID;

public class AltimitConnector extends Thread{
    private ServerSocket socket;
    private static AltimitConnector instance;
    private int port;
    private AltimitNetwork altimitNetwork = AltimitNetwork.getInstance();

    static synchronized AltimitConnector getInstance(){

        if (instance== null) {
            instance = new AltimitConnector();
        }
        return instance;
    }

    boolean isRunning(){
        return socket.isClosed();
    }

    public void StopAltimitServer(){
        for(UUID clientUUID : altimitNetwork.localClientMap.keySet()){
            altimitNetwork.DisconnectUser(clientUUID, true);
        }

        try {
            socket.close();
            System.out.println("Altimit Server has been stopped...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    synchronized void start(int port) {
        this.port = port;
        try {
            System.out.printf("Starting server on port {}", port);
            socket = new ServerSocket(port);
        } catch (IOException e) {
            return;
        }
        super.start();
    }

    @Override
    public void run() {
        while(isRunning()){
            try {
                Thread.sleep(100);
                listenForConnections();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void listenForConnections(){
        try {
            Socket newConnection = socket.accept();
            System.out.printf("Accepting connection from {}", newConnection.getInetAddress().getHostAddress());
            AddClientToAltimitNetwork(newConnection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This sets the UUID of the client. This UUID is used to identify them throught the whole process while they are connected.
     * @return retuns the UUID and that is used to set the clientUUID variable
     */
    private void AddClientToAltimitNetwork(Socket newConnection){
        UUID newUUID = UUID.randomUUID();

        if(altimitNetwork.localClientMap.containsKey(newUUID)){
            System.out.println("UUID has already been registered! Dropping client!...");
            AddClientToAltimitNetwork(newConnection);
        } else {
            ClientInfo clientTemp = new ClientInfo(newConnection);
            altimitNetwork.localClientMap.put(newUUID, clientTemp);

            Users.Add(newUUID);

            System.out.println("Clients UUID has been set...");
        }
    }
}
