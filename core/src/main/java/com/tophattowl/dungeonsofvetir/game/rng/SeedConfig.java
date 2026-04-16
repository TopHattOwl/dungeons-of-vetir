package com.tophattowl.dungeonsofvetir.game.rng;

public class SeedConfig {
    private final long seed;
    private final SeedSource source;

    private SeedConfig(long seed, SeedSource source) {
        this.seed = seed;
        this.source = source;
    }

    public static SeedConfig random() {
        return new SeedConfig(System.currentTimeMillis(), SeedSource.RANDOM);
    }

    public static SeedConfig custom(long seed) {
        return new SeedConfig(seed, SeedSource.CUSTOM);
    }

    public static SeedConfig fromSave(long seed) {
        return new SeedConfig(seed, SeedSource.SAVED);
    }

    public long getSeed() {
        return seed;
    }

    public SeedSource getSource() {
        return source;
    }
}
