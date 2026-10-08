package com.tophattowl.dungeonsofvetir.game.generation;

/**
 * Stable hashing for seeds. Uses only specified primitives ({@code String.hashCode},
 * {@code ordinal}, ints) - never {@code Object}/{@code record}/{@code enum} identity
 * hashes, which are not stable across JVM runs and would break determinism/save/load.
 */
public final class HashUtil {

    private static final long GOLDEN = 0x9E3779B97F4A7C15L;

    private HashUtil() {}

    public static long mix(long seed, int value) {
        long h = seed + GOLDEN + value;
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        return h;
    }

    public static long mix(long seed, String value) {
        return mix(seed, value == null ? 0 : value.hashCode());
    }

    public static long mix(long seed, Enum<?> value) {
        return mix(seed, value == null ? 0 : value.ordinal());
    }

    public static long mix(long seed, int x, int y) {
        return mix(mix(seed, x), y);
    }
}
