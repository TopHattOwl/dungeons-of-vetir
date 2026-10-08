package com.tophattowl.dungeonsofvetir.game.world;

/**
 * Stable identity for a location, used by exits and (later) quests/contracts.
 * Named places carry a human key ("ashford", "grimhold"); procedural ones derive
 * it from their coordinates.
 *
 * @param key   for named places the content key; empty for generic zones
 * @param depth floor depth within a dungeon, or 0 for other kinds
 */
public record PlaceId(PlaceKind kind, String key, int depth, int worldX, int worldY) {

    public static PlaceId overworld() {
        return new PlaceId(PlaceKind.OVERWORLD, "", 0, 0, 0);
    }

    public static PlaceId settlement(String key, int worldX, int worldY) {
        return new PlaceId(PlaceKind.SETTLEMENT, key, 0, worldX, worldY);
    }

    public static PlaceId biome(int worldX, int worldY) {
        return new PlaceId(PlaceKind.BIOME, "", 0, worldX, worldY);
    }

    public static PlaceId dungeonSurface(String dungeonKey) {
        return new PlaceId(PlaceKind.DUNGEON_SURFACE, dungeonKey, 0, 0, 0);
    }

    public static PlaceId dungeonFloor(String dungeonKey, int depth) {
        return new PlaceId(PlaceKind.DUNGEON_FLOOR, dungeonKey, depth, 0, 0);
    }

    public boolean isDungeonFloor() {
        return kind == PlaceKind.DUNGEON_FLOOR;
    }

    /**
     * The template key (before any {@code @origin} suffix) - the content key used to
     * look up side-room/biome/settlement definitions. Instance keys encode their
     * origin so several generated places can share a template.
     */
    public String templateKey() {
        int at = key.indexOf('@');
        return at < 0 ? key : key.substring(0, at);
    }
}
