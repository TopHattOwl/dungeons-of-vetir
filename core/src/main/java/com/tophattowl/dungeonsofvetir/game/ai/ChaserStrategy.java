package com.tophattowl.dungeonsofvetir.game.ai;

import com.tophattowl.dungeonsofvetir.game.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.AiComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaserStrategy extends AiStrategy {
    private static final Random RANDOM = new Random();

    public ChaserStrategy() {
        super(10);
    }

    @Override
    public Action chooseAction(Entity entity, GameWorld world) {
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        FovComponent fovComp = entity.getComponent(FovComponent.class);
        AiComponent aiComp = entity.getComponent(AiComponent.class);

        if (posComp == null || fovComp == null || aiComp == null) {
            return new PassAction(entity);
        }

        Entity player = world.getPlayer();
        if (player == null) {
            return new PassAction(entity);
        }

        PositionComponent playerPosComp = player.getComponent(PositionComponent.class);
        if (playerPosComp == null) {
            return new PassAction(entity);
        }

        int playerX = playerPosComp.getX();
        int playerY = playerPosComp.getY();
        int entityX = posComp.getX();
        int entityY = posComp.getY();

        boolean canSeePlayer = fovComp.isVisible(playerX, playerY);

        if (canSeePlayer) {
            aiComp.lastKnownPlayerPos = new Point(playerX, playerY);

            DijkstraMap dijkstraMap = world.getDijkstraMap();
            if (dijkstraMap != null) {
                Direction dir = dijkstraMap.getDirection(entityX, entityY);
                if (dir != Direction.STAY) {

                    return new MoveAction(dir, entity);
                }
            }

            Direction directDir = getDirectDirection(entityX, entityY, playerX, playerY);
            if (directDir != Direction.STAY) {
                return new MoveAction(directDir, entity);
            }
        } else {
            if (aiComp.lastKnownPlayerPos != null) {
                int lastX = aiComp.lastKnownPlayerPos.x;
                int lastY = aiComp.lastKnownPlayerPos.y;

                if (entityX == lastX && entityY == lastY) {
                    aiComp.lastKnownPlayerPos = null;
                } else {
                    DijkstraMap dijkstraMap = world.getDijkstraMap();
                    if (dijkstraMap != null) {
                        Direction dir = dijkstraMap.getDirection(entityX, entityY);
                        if (dir != Direction.STAY) {
                            return new MoveAction(dir, entity);
                        }
                    }

                    Direction directDir = getDirectDirection(entityX, entityY, lastX, lastY);
                    if (directDir != Direction.STAY) {
                        return new MoveAction(directDir, entity);
                    }

                    aiComp.lastKnownPlayerPos = null;
                }
            }
        }

        return wander(entity, world);
    }

    private Action wander(Entity entity, GameWorld world) {
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        if (posComp == null) {
            return new PassAction(entity);
        }

        Level level = world.getCurrentLevel();
        int x = posComp.getX();
        int y = posComp.getY();

        List<Direction> walkableDirs = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir == Direction.STAY) continue;

            int nx = x + dir.getDx();
            int ny = y + dir.getDy();

            if (level.isInBounds(nx, ny) && level.isWalkable(nx, ny)) {
                if (world.getEntityAt(nx, ny) == null) {
                    walkableDirs.add(dir);
                }
            }
        }

        if (walkableDirs.isEmpty()) {
            return new PassAction(entity);
        }

        Direction chosen = walkableDirs.get(RANDOM.nextInt(walkableDirs.size()));
        return new MoveAction(chosen, entity);
    }

    private Direction getDirectDirection(int fromX, int fromY, int toX, int toY) {
        int dx = Integer.signum(toX - fromX);
        int dy = Integer.signum(toY - fromY);

        if (dx == 0 && dy == 0) return Direction.STAY;

        for (Direction dir : Direction.values()) {
            if (dir.getDx() == dx && dir.getDy() == dy) {
                return dir;
            }
        }

        return Direction.STAY;
    }
}
