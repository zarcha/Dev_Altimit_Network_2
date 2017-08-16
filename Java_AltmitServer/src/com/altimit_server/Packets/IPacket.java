package com.altimit_server.Packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IPacket {
    public void serialize(DataOutputStream outputStream) throws IOException;
    public void deserialize(DataInputStream inputStream) throws IOException;
}
