package de.javasocketapi.core;

import java.util.ArrayList;
import java.util.List;

public class PacketRegistry {

    private static final List<Class<? extends Packet>> registerdPackets = new ArrayList<>();

    static int indexOf(final Class<? extends Packet> packetClass) {
        return PacketRegistry.registerdPackets.indexOf(packetClass);
    }

    static Class<? extends Packet> get(final int index) {
        return PacketRegistry.registerdPackets.get(index);
    }

    public static void registerPacket(final Class<? extends Packet> packetClass) {
        //register packet
        PacketRegistry.registerdPackets.add(packetClass);
    }

    public static void registerPackets(final List<Class<? extends Packet>> packetClasses) {
        //register packets
        PacketRegistry.registerdPackets.addAll(packetClasses);
    }

}
