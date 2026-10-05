# Dungeons of Vetir — Codebase Audit

A working assessment of the project: what exists, what is incomplete, and where the
architecture fights future expansion. Written to be a reference for planned refactors.

Phased plan:
- Phase 1 (done/in progress): correctness landmines in actions, combat, equipment.
- Phase 2: structural — de-staticize, system pipeline, Level owns size/spatial index, split GameWorld.
- Phase 3: expandability — dispatch registries, damage resolver, stat aggregation, Rng service.
- Phase 4: features — inventory/ground items, spawning director, magic/ranged, save/load, bosses/NPCs.

## 1. Status by system

### Implemented
- Time/energy turn scheduler (`TimeTurnManager`, `TurnEvent`, `TimeValueComponent`, per-action costs).
- Event bus with subscription handles (`EventBus`, `EventSubscriptions`).
- ECS core + data-driven factories (`Entity`/`Component`, `ActorSpec`/`ItemTemplate` + specs, registries).
- Recursive shadowcasting FOV with explored memory (`FovSystem`).
- Dijkstra maps: player + per-faction flow fields, faction-aware goal blocking, overlay.
- Section-based level generation: `WorldLayout` auto-derived spans/rest floors, one variation per section,
  `LevelGenerator` strategy (`CaveGenerator`, `RoomGenerator`), deterministic seeds, `FloorRole`.
- Theme/asset layer: `AssetTileset`/`PaletteTileset`/`TerrainTilesetRegistry`, `SpriteLibrary`,
  `TextureRegistry`, Nearest filtering, magenta missing fallback.
- Rendering: fixed virtual resolution scaled with `FitViewport`, HUD, Scene2D debug console.
- Floor traversal: `DescendAction`/`AscendAction`, `GameWorld.enterLevel`, `warp`.

### Partial
- Bodypart combat: model is solid, but armor/natural protection unused, death ordering buggy,
  bodypart condition damage is scaled twice (intended: per-part `damageMultiplier` x global `0.4`).
- Items/equipment: equip/unequip/swap slot mechanics now work (incl. two-handed and armor slots);
  armor *content* (components, registry items, stats) is still absent; no inventory/ground items.
- AI: Dijkstra movement + faction avoidance; no brain (`EntityBrain` empty); attacks only by walking in.
- Factions: relations exist and steer movement; little else.
- Spawning: a budget model exists but nothing drives it, plus a second spawn path (`LevelPopulator`).
- Stats: `OffensiveStatsComponent`/`DefensiveStatsComponent` half-used.
- RNG: only dungeon generation is seeded; the rest is unseeded.
- Tests: some strong, but `integration/MovementSystemTest` does not test `MovementSystem`, and
  combat/equip/spawn lacked end-to-end coverage.

### Not implemented
- Bosses (miniboss floors marked, empty), NPCs/quests, deep magic, classless skills/progression,
  inventory UI + carrying, ranged attacks/projectiles, save/load (`SeedSource.SAVED` unused),
  menus/other screens, audio, closed doors.

## 2. Architectural problems

### Critical correctness
1. Action contract permitted `null` — `SwapEquipmentAction`, armor branches, `UnequipAction` (not an
   `Action`). `ActionHandler.executeAction`/`GameScreen.input` assumed non-null. (addressed Phase 1)
2. Death ordering — damage applied in a loop; `die()` could remove the target mid-loop. (addressed Phase 1)
3. Bodypart condition double-scale — per-part multiplier and global `0.4`; now intentional and documented.

### High-impact structural
4. Global singleton state: `ActionHandler` static `GameWorld`, `CombatCalculators`, `FactionRelation`,
   `EventBus`, `DebugLogger`. Hurts testing and multiple worlds.
5. `GameWorld` is a god object: entities, spatial map, level/floor, turn manager, Dijkstra manager,
   spawn registry, FOV system, player.
6. ECS isn't systemic: `GameSystem` mostly unused; systems are static method bags; no pipeline.
7. Components keyed by exact class — no interface/supertype queries or change notifications.
8. Level dimensions are static/global (`Level.WIDTH/HEIGHT`) — blocks variable-size/multiple levels.
9. AI lives in the turn manager; `EntityBrain` empty.
10. No dispatch registries for attack types/actions; `AttackCalculator.getType()` unused.
11. No damage pipeline: `protections`/`resistances`/`naturalProtection` unused; block/counter bypass armor.
12. Two competing spawn systems, one dormant with dead code.
13. Equipment model fragile: side-effecting `EquipmentComponent`, empty handlers, no stat recompute.
14. Items don't exist in the world; inventory is a stub.
15. RNG not centralized.
16. Dijkstra manager ownership split between `GameScreen` and `GameWorld`.

### Lower severity
- Two UI stacks (hand-rolled HUD + Scene2D console).
- `GameScreen` mixes bootstrap/wiring/debug leftovers.
- Monster FOV computed and unused.
- Input numpad-only.
- `Level.getTile` allocates on out-of-bounds; `GameWorld.query`/`getEntity(id)` are O(n).
- `RegenComponent` uses exceptions for control flow.
- Static `nextId` in `Entity`/`Item`.
- Misnamed/incomplete tests.

## 3. Target direction

- Per-world context (`EventBus`, `Rng`, registries, system list) — no statics.
- System pipeline run by the turn scheduler.
- `Level` owns size + tiles + spatial index; `GameWorld` becomes a thin facade.
- Registry dispatch for `AttackType` -> calculator, `ActionType` -> handler, `TileType` -> behavior.
- Single damage resolver (coverage/penetration/resistance then bodypart, correct death ordering).
- Equipment changes emit events; a stat aggregator derives current stats.
- One spawn director wired to the turn clock.
- Inventory + ground items as entities.
- Injected `Rng`.

## 4. Sequencing

- Phase 1 — correctness and safety (low risk): action contract; death ordering; bodypart scale documented;
  equip/unequip/swap slot mechanics implemented (armor content still absent); debug leftovers removed.
- Phase 1.5 — type-safe equip handlers (`EquipHandler<T>` declares its component; fallback removed) and
  `EquipmentChangedEvent` emitted on every equipment change (hook for the future stat aggregator).
- Phase 2 — structural (medium risk): `WorldContext`, system pipeline, `Level` dimensions/spatial index,
  split `GameWorld`.
- Phase 3 — expandability (medium risk): dispatch registries, damage resolver, stat aggregation, `Rng`.
- Phase 4 — features: inventory/ground items, spawning director, magic/ranged, save/load, content.
