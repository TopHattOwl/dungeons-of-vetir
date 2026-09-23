# Dungeons of Vètir

A traditional, turn-based roguelike dungeon crawler in Java, built on libGDX. Fantasy horror, no classes, and a combat system where a severed arm is a problem you can play around instead of a number that goes down.

It's a work in progress and a learning project. I'm building it because I love traditional roguelikes and because I can, and to get better at writing Java that stays pleasant to extend. The art is 16x24 pixels and drawn by me, so it's ugly on purpose. Mostly.

## The game

You descend through procedurally generated floors, fight things, and try not to die. The setting is fantasy horror and the worldbuilding is very much in progress, so treat everything below as a promise rather than a description.

Design pillars:

- **Bodypart-based combat.** Every creature has a body whose parts have their own condition. Heads are vital. Tails are not. Losing a limb changes the fight instead of just lowering a bar.
- **Classless progression.** No character classes. Your character is what you're wearing and what you've practiced.
- **Magic cast from gear.** Spells are channelled through weapons and armor, and the item shapes the spell. A fire spell from a spear becomes a long line; from a sword, a wide arc. Armor bends it too: light armor toward agility, heavy armor toward raw protection.
- **Factions.** Creatures belong to factions with relations to each other

## What's in the dungeon today

**Done**

- A turn system on a time/energy clock, where every action costs time and the world advances between your turns.
- Field of view via recursive shadowcasting, with remembered (explored) tiles.
- Staircases between floors, and a section layout that changes as you go deeper.

**WIP**

- Bodypart-based combat and multiple bodytypes. Injury system.
- Procedural floors. Two sections (caves, ruins) with variations and a room-and-corridor generator exist. More themes and special floors in progress.
- Factions and relations. Relations exist and drive AI movement, but the system needs improvement.
- Items and equipment. Weapons, wield slots and equip handlers are in, no fully implemented inventory system yet.
- Faction-aware AI: currently only uses Dijkstra maps, EntityBrain is coming when I feel like tackling it.

**Planned**

- Bosses. Miniboss floors are marked out in the layout; nothing lives there yet.
- NPCs and a questline.
- The deep magic system.
- Classless skill and stat progression.

**Needs love**

- The 16x24 hand-made pixel art, and there are about 4 sprites that I am proud of. 

## Under the hood

The parts I'm happy with, and why they're built the way they are.

- **Custom ECS with data-driven factories.** Entities are IDs with a map of components; actors and items are described by specs and registered in a registry, so adding a monster or an item is one entry. The same idea runs the actor, item and section registries.
- **A time-based turn system.** Actors sit in a priority queue keyed by accumulated time, and a separate world "turn event" acts as the clock. Actions carry a time cost, so fast and slow creatures fall out of the same mechanism with no special cases.
- **An event bus with subscription handles.** Combat, UI, spawning and the turn clock talk through events instead of holding references to each other. Subscriptions hand back a handle, so nothing leaks when a system goes away.
- **Recursive shadowcasting FOV** with a separate explored-tile memory, so the map remembers what you've seen.
- **Dijkstra-map AI with factions.** Each faction gets its own flow-field map, recomputed as the world changes.
- **Pluggable, data-driven level generation.** A run is a list of sections, each with a set of variations and a floor length; the floor spans and the rest floors between sections are derived, not typed out. A section maps to a `LevelGenerator` strategy (caves use cellular automata, ruins use rooms and corridors), and generation is seeded, so the same seed gives the same dungeon.
- **Two-phase actions.** Every action is prepared (can it happen?) and then executed (does it, and what does it cost?), which keeps validation and effect in separate, testable places.
- **A bodypart injury model.** Each part has its own condition, its own chance to be hit, and a role (vital, limb, appendage) that decides what losing it means. Hit location and damage type are separate concerns from raw health.
- **Rendering at a fixed virtual resolution.** The game is laid out at 1280x800 and scaled to the window with a fit viewport, so resizing scales everything together instead of showing more or less of the world.

## Project layout

- `core` - all the game logic and rendering. Cross-platform, no launcher code.
- `lwjgl3` - the desktop launcher.

## Running it

```bash
./gradlew lwjgl3:run   # play
./gradlew test         # run the tests
./gradlew build        # build everything
```

Requires JDK 17.

## Controls

- Numpad: move, and attack by walking into something hostile
- `.` or `>` - take the stairs down
- `,` or `<` - take the stairs up
- Backtick - toggle the debug console; type `help` for commands


## A note

This is a personal project, it's under active construction, and I'm only doing it because of my love of game development and traditional roguelikes, and to learn building system from the ground up. If you're here because you're learning libGDX or roguelike architecture, I hope the "under the hood" section is useful.
