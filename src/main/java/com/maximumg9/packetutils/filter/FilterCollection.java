package com.maximumg9.packetutils.filter;

import com.maximumg9.packetutils.PacketUtils;
import com.maximumg9.packetutils.util.PacketData;
import net.minecraft.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public class FilterCollection <F extends Filter<? extends Packet<?>>> {
    private final Map<Class<? extends Packet<?>>, F> filterList = new HashMap<>();
    private final FilterCreator<F> filterSupplier;

    public FilterCollection(FilterCreator<F> filterSupplier) {
        this.filterSupplier = filterSupplier;
    }

    public F getFilter(Class<? extends Packet<?>> clazz) {
        if(filterList.containsKey(clazz)) {
            return filterList.get(clazz); // this cast doesn't fail
        } else {
            F newFilter = filterSupplier.create(clazz); // this cast doesn't fail
            filterList.put(clazz,newFilter);
            return newFilter; // this cast doesn't fail
        }
    }

    @SuppressWarnings("unchecked")
    public <P extends Packet<?>> Packet<?> apply(P packet) {
        if(filterList.containsKey(packet.getClass())) {
            F filter = getFilter(PacketUtils.getClass(packet));
            return ((Filter<P>) filter).apply(new PacketData<>(packet)).recreate(); // this cast doesn't fail
        } else {
            return packet;
        }
    }
}
