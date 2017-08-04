package com.altimit_server;

import com.altimit_server.ControlPanel.AltimitHttpServer;
import com.altimit_server.ControlPanel.AltimitRest;
import com.altimit_server.types.ClientInfo;
import com.altimit_server.util.AltimitConverter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.*;

import com.altimit_server.util.PropertiesManager;

/**
 * This class handles the listening of clients connections, compiling all AltimitCmd classes, and connecting to hazelcast.
 */
public class main {
    /** This is the map that links UUID's of each client to their socket. **/
    static Map<UUID, ClientInfo> localClientMap = new HashMap<>();

    private static PropertiesManager propertiesManager = new PropertiesManager(main.class);

    private static ServerSocket listener;

    /** Main method of the server **/
    public static void main(String[] args){


        //Start server
        try {
            StartServer();
        }catch (Exception e){

        }
    }

    /**
     * Starts the connection to hazelcast, compiles AltimitCmd.
     */
    private static void StartServer() throws Exception{
        //lets just do something fancy to show its ready
        System.out.println("==================================== \n" +
                            "========    ALTIMIT SERVER  ======== \n" +
                            "==================================== \n");


        if(propertiesManager.useRestService()) {
            //Start Rest Server
            AltimitRest.StartAll();
            AltimitHttpServer.StartHttpServer(propertiesManager);
        }

        //Compile a list of the methods that will be used when compiling
        AltimitMethod.AltimitMethodCompile();
        AltimitHeartBeat.StartChecks();


        StartAltimitServer();
    }

    public static void StartAltimitServer()  throws Exception{
        //Start listening for clients1
        listener = new ServerSocket(propertiesManager.getServerPort());

        //Let the admin know that we can accept users now
        System.out.println("Ready for clients...");

        try {
            while (!listener.isClosed()){
                //once a client connects do things with them in their own thread
                new Handler(listener.accept()).start();
            }
        } finally {
            if(!listener.isClosed()) {
                listener.close();
            }
        }
    }

    /**
     * This class handles each places network actions using individual threads
     */
    private static class Handler extends Thread {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        /**
         * This is the key needed for a message to be valid
         */
        byte[] key = {5, 9, 0, 4};

        int messageSize = 0;
        int messageOffset;
        byte[] currentMessage;

        //message that can be old and new
        int fullMessageSize;
        byte[] fullMessage = new byte[0];

        /**
         * This is to see if the client has registered its UUID with the server
         */
        UUID clientUUid;

        Integer bufSize = 0;

        /**
         * Sets the socket passed to the thread.
         * @param socket The socket of the connected client.
         */
        Handler(Socket socket){
            this.socket = socket;
        }

        /**
         * This is automatically ran when the thread is started. It will handle the running, parsing, conversion and calling of messages sent by clients.
         */
        public void run() {
            System.out.println("Client has connected from " + socket.getLocalSocketAddress().toString());

            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            }catch (IOException e){
                System.out.printf("in or out failed: ", e);
            }

            try{
                while (true) {
                        bufSize = in.available();
                        if (bufSize != 0 || fullMessage.length != 0) {

                            //Lets see if this is an old message, new message, and if it needs to combine with an old message
                            if (fullMessage.length != 0 && bufSize != 0) {
                                fullMessage = Arrays.copyOf(fullMessage, fullMessage.length + bufSize);
                                byte[] newMessage = new byte[bufSize];
                                in.read(newMessage, 0, bufSize);
                                System.arraycopy(newMessage, 0, fullMessage, fullMessageSize, newMessage.length);
                                messageSize = 0;
                            } else if (bufSize != 0) {
                                fullMessage = new byte[bufSize];
                                in.read(fullMessage, 0, bufSize);
                                messageSize = 0;
                            } else if (fullMessage.length != 0) {
                                messageSize = 0;
                            }

                            //If the message is not empty now then lets continue
                            if (messageSize == 0) {
                                //Lets get the size of the next message
                                messageSize = ByteBuffer.wrap(fullMessage, 0, 4).getInt();

                                //Lets make sure this message is long enough to even check for a key
                                if(messageSize <= fullMessage.length) {

                                    //Get the last 4 bytes (should be the key)
                                    byte[] messageKey = Arrays.copyOfRange(fullMessage, messageSize - 4, messageSize);

                                    //Make sure the full message is here
                                    if (Arrays.equals(messageKey, key)) {
                                        currentMessage = new byte[messageSize - 8];
                                        messageOffset = 4;
                                        System.arraycopy(fullMessage, messageOffset, currentMessage, 0, currentMessage.length);
                                        messageOffset = messageSize;

                                        //Lets get the convert the byte array message to a list of real variables
                                        List<Object> tSentMessage = AltimitConverter.ReceiveConversion(currentMessage);

                                        tSentMessage.add(clientUUid);
                                        //Make sure this client has an identifier and if it doesnt then see if it is trying to set it. If not then do nothing!
                                        if (clientUUid != null) {
                                            AltimitInvoker tAltimitTask = new AltimitInvoker(tSentMessage);
                                            new Thread(tAltimitTask).start();

                                        } else {
                                            MakeClientUUID();
                                        }

                                        //if there is more of a message then delete the stuff we just used and if not then reset data
                                        if (messageOffset != fullMessage.length) {
                                            fullMessage = Arrays.copyOfRange(fullMessage, messageOffset, fullMessage.length);
                                            fullMessageSize = fullMessage.length;
                                        } else {
                                            fullMessage = new byte[0];
                                        }
                                    } else {
                                        System.out.println("Key was not found. Message will try to be completed in next read!");
                                    }
                                }
                            }
                        }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                //If they crashed the party then kick them out
                DisconnectUser(socket, out, in, Thread.currentThread());
                System.out.println("run");
            }
        }


        /**
         * This is used to set the UUID of the client so we can identify it and send its socket messages or delete it.
         * This is also used for pretty much every function since its the main way of looking up information on clients.
          */
        void MakeClientUUID(){
            UUID newUUID = UUID.randomUUID();

            if(localClientMap.containsKey(newUUID)){
                System.out.println("UUID has already been registered! Dropping client!...");
                MakeClientUUID();
            } else {
                clientUUid = newUUID;

                ClientInfo clientTemp = new ClientInfo(socket, out, in, Thread.currentThread());
                localClientMap.put(newUUID, clientTemp);
                System.out.println("Clients UUID has been set...");

                Users.Add(newUUID);
            }
        }
    }

    /**
     * If the client is not needed anymore this is used to disconnect the client.
     * @param clientUUID The UUID of the client to disconnect. This will be used to find all data related to the client and remove it.
     * @param server This is used to tell if the disconnect is because of the server or because the client disconnected.
     */
    public static void DisconnectUser(UUID clientUUID, Boolean server){
        ClientInfo clientInfo = localClientMap.get(clientUUID);

        if(server){
            PostMan.SendPost(clientUUID, "Disconnect");
        }

        try {
            clientInfo.socket.close();
            clientInfo.clientThread.interrupt();
        } catch (IOException e) {
            System.out.println("Error closing client socket.");
        }

        localClientMap.remove(clientUUID);
        Users.Remove(clientUUID);

        System.out.println("User " + clientUUID.toString() + " has been disconnected.");
    }

    /**
     * Handles the disconnection of a client that has no data set and does the clean-up needed.
     * @param socket The socket to disconnect
     * @param outputStream The out
     * @param inputStream
     * @param clientThread
     */
    static void DisconnectUser(Socket socket, DataOutputStream outputStream, DataInputStream inputStream, Thread clientThread){
        try{
            socket.close();
            clientThread.interrupt();
        }catch (IOException e) {
            System.out.println("Error Disconnecting: " + e.toString());
        }

        System.out.println("User has been disconnected.");
    }

    public static boolean isRunning(){
        return !listener.isClosed();
    }

    public static void StopAltimitServer(){
        for(UUID clientUUID : localClientMap.keySet()){
            DisconnectUser(clientUUID, true);
        }

        try {
            listener.close();
            System.out.println("Altimit Server has been stopped...");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
