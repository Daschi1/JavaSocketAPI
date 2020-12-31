package de.javasocketapi.core;

import java.util.ArrayList;
import java.util.List;

public class PacketRegistry {

    private static final List<Class<? extends Packet>> registeredPackets = new ArrayList<>();

    static int indexOf(final Class<? extends Packet> packetClass) {
        return PacketRegistry.registeredPackets.indexOf(packetClass);
    }

    static Class<? extends Packet> get(final int index) {
        return PacketRegistry.registeredPackets.get(index);
    }

    public static void registerPacket(final Class<? extends Packet> packetClass) {
        //register packet
        PacketRegistry.registeredPackets.add(packetClass);
    }

    public static void registerPackets(final List<Class<? extends Packet>> packetClasses) {
        //register packets
        PacketRegistry.registeredPackets.addAll(packetClasses);
    }

}
