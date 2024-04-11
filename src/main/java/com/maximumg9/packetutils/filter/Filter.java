package com.maximumg9.packetutils.filter;

import com.maximumg9.packetutils.util.PacketData;
import net.minecraft.network.packet.Packet;

public interface Filter<T extends Packet<?>> {
    PacketData<T> apply(PacketData<T> packet);
}
