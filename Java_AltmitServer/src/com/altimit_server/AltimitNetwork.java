package com.altimit_server;

import com.altimit_server.ControlPanel.AltimitHttpServer;
import com.altimit_server.ControlPanel.AltimitRest;
import com.altimit_server.types.ClientInfo;
import com.altimit_server.types.User;
import com.altimit_server.util.PropertiesManager;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AltimitNetwork extends Thread {

    /** This is the map that links UUID's of each client to their socket. **/
    static Map<UUID, ClientInfo> localClientMap = new HashMap<>();

    private  PropertiesManager propertiesManager = new PropertiesManager(main.class);
    private  static AltimitNetwork instance;

    private AltimitRest altimitRest = new AltimitRest();
    private AltimitHeartBeat altimitHeartBeat = new AltimitHeartBeat();

    private Map<Socket, BufferData> bufferMap;
    private ReadWriteLock clientLock = new ReentrantReadWriteLock();

    /**
     * Starts the connection to hazelcast, compiles AltimitCmd.
     */
    public void StartServer() throws Exception{
        //lets just do something fancy to show its ready
        System.out.println("==================================== \n" +
                "========    ALTIMIT SERVER  ======== \n" +
                "==================================== \n");

        StartAltimitServer();

        if(propertiesManager.useRestService()) {
            //Start Rest Server
            altimitRest.StartAll();
            AltimitHttpServer.StartHttpServer(propertiesManager);
        }

        //Compile a list of the methods that will be used when compiling
        altimitHeartBeat.StartChecks();
    }

    public void StopAltimitServer(){
        for(UUID clientUUID : localClientMap.keySet()){
            DisconnectUser(clientUUID, true);
        }

        AltimitConnector.StopServer();
    }

    public void StartAltimitServer(){
        //Let the admin know that we can accept users now
        System.out.println("Ready for clients...");

        AltimitConnector.Start(propertiesManager.getServerPort());
        localClientMap = new HashMap<>();
        super.run();
    }

    @Override
    public void run(){
        while(AltimitConnector.isRunning()){
            try{
                currentThread().sleep(1000);
                receiverData();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void receiverData(){
        try {
            clientLock.readLock().lock();
            for(ClientInfo client : localClientMap.values()){
                Socket currentSocket = client.socket;
                InputStream stream = currentSocket.getInputStream();
                if (stream.available() > 0) {
                    BufferData data = getBuffer(currentSocket);
                    if (data.read(stream))
                    {
                        bufferMap.remove(currentSocket);//We're done with that buffer, a new one will be created if needed.
                        /*PacketManager.getInstance().handlePacket(new DataInputStream(new ByteArrayInputStream(data.data.toByteArray())));*/
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientLock.readLock().unlock();
        }
    }

    private BufferData getBuffer(Socket socket) throws IOException {
        if (bufferMap.containsKey(socket))
            return bufferMap.get(socket);

        BufferData data  = new BufferData(socket.getInputStream().read());
        return data;
    }


    private class BufferData {
        ByteArrayOutputStream data;
        int size;

        BufferData(int size){
            data = new ByteArrayOutputStream();
            this.size=size;
        }

        boolean read(InputStream stream) throws IOException {
            int available = stream.available();
            byte[] in = new byte[Math.min(size, available)];
            stream.read(in);
            data.write(in);
            size -= available;
            if (size <= 0)
                return true;
            return false;
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
            /*PostMan.SendPost(clientUUID, "Disconnect");*/
        }

        try {
            clientInfo.socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client socket.");
        }

        localClientMap.remove(clientUUID);
        Users.Remove(clientUUID);

        System.out.println("User " + clientUUID.toString() + " has been disconnected.");
    }

    public static boolean isRunning(){
        return AltimitConnector.isRunning();
    }
}
