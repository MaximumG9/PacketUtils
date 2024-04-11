package com.maximumg9.packetutils;

import com.maximumg9.packetutils.filter.FilterCollection;
import com.maximumg9.packetutils.filter.LoggingFilter;
import net.minecraft.network.packet.Packet;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Stream;

/*
 * Making Custom descriptions
 * Pros:
 * Easier to use first time
 * Easier to browse through
 * Easier to understand the protocol
 *
 * Cons:
 * It takes a lot of time to make
 * 182 Descriptions
 * LIKE ACTUALLY THAT MANY
 *
 */
public class PacketUtils {

    public static final FilterCollection<LoggingFilter<? extends Packet<?>>> LOG_FILTER_COLLECTION =
            new FilterCollection<>(new LoggingFilter.Supplier());
    public static <T extends Packet<?>> Field[] getAllFields(Class<T> clazz) {
        return Stream.concat(
                Arrays.stream(clazz.getDeclaredFields()),
                Arrays.stream(clazz.getSuperclass().getDeclaredFields())
        ).toArray(Field[]::new);
    }

    @SuppressWarnings("unchecked")
    public static <T, U extends T> Class<U> getClass(T object) {
        return (Class<U>) object.getClass();
    }
}
