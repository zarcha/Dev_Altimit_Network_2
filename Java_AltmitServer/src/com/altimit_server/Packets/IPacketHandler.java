package com.altimit_server.Packets;

public interface IPacketHandler<T extends IPacket> {
    public void handle(T Packet);
}
