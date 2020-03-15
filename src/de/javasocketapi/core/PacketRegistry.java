package de.javasocketapi.core;

import java.util.ArrayList;
import java.util.List;

public class PacketRegistry {

    private static List<Class<? extends Packet>> registerdPackets = new ArrayList<>();

    static int indexOf(Class<? extends Packet> packetClass) {
        return PacketRegistry.registerdPackets.indexOf(packetClass);
    }

    static Class<? extends Packet> get(int index) {
        return PacketRegistry.registerdPackets.get(index);
    }

    public static void registerPacket(Class<? extends Packet> packetClass) {
        PacketRegistry.registerdPackets.add(packetClass);
    }

    public static void registerPackets(List<Class<? extends Packet>> packetClasses) {
        PacketRegistry.registerdPackets.addAll(packetClasses);
    }

}
