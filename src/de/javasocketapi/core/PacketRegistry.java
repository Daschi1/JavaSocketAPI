package de.javasocketapi.core;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

class PacketRegistry {

    private static List<Class<? extends Packet>> registerdPackets;

    public static int indexOf(Class<? extends Packet> packetClass) {
        return registerdPackets.indexOf(packetClass);
    }

    public static Class<? extends Packet> get(int index) {
        return registerdPackets.get(index);
    }

    static {
        Reflections reflections = new Reflections();
        PacketRegistry.registerdPackets = new ArrayList<>(reflections.getSubTypesOf(Packet.class));
    }

}
