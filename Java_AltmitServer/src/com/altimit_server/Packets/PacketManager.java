package com.altimit_server.Packets;

import com.altimit_server.util.PacketUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PacketManager {
    private static PacketManager instance;
    private Map<Class<? extends IPacket>, IPacketHandler> packetHandlers;

    public synchronized static PacketManager getInstance(){
        if (instance == null)
            instance=new PacketManager();
        return instance;
    }

    private PacketManager(){
        this.packetHandlers = new HashMap<>();
    }

    public void registerHandler(Class<? extends IPacket> packet, IPacketHandler handler ){
        packetHandlers.put(packet,handler);
    }

    public void handlePacket(DataInputStream stream){
        try {
            String packetName = PacketUtils.readString(stream);
            for(Class clazz : packetHandlers.keySet()){
                if (clazz.getName().equals(packetName)) {
                    IPacket packet = (IPacket) clazz.newInstance();
                    packet.deserialize(stream);
                    packetHandlers.get(clazz).handle(packet);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
