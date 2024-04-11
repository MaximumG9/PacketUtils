package com.maximumg9.packetutils.filter;

import net.minecraft.network.packet.Packet;

public interface FilterCreator<F extends Filter<?>> {
    <T extends Packet<?>> F create(Class<T> clazz);
}
