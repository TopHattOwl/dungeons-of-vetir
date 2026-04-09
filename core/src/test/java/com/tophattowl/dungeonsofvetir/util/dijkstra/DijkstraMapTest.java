package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.DijkstraMap;
import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.PlayerDijkstraMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DijkstraMapTest {

    @Test
    void constructor_InitializesArray() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        assertNotNull(map.map);
        assertEquals(10, map.map.length);
        assertEquals(10, map.map[0].length);
    }

    @Test
    void calculate_ObstacleStaysObstacle() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        map.map[5][5] = DijkstraMap.OBSTACLE_VALUE;
        map.calculate();
        assertEquals(DijkstraMap.OBSTACLE_VALUE, map.map[5][5]);
    }

    @Test
    void calculate_GoalIsZero() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.calculate();
        assertEquals(DijkstraMap.GOAL_VALUE, map.map[5][5]);
    }

    @Test
    void calculate_PropagatesCorrectly() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.map[0][0] = DijkstraMap.BASE_VALUE;

        map.calculate();

        assertEquals(DijkstraMap.GOAL_VALUE, map.map[5][5]);
        assertTrue(map.map[4][5] <= 2 || map.map[6][5] <= 2 || map.map[5][4] <= 2 || map.map[5][6] <= 2);
    }

    @Test
    void calculate_CardinalNeighborCost() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                map.map[x][y] = DijkstraMap.BASE_VALUE;
            }
        }
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.calculate();

        int northValue = map.map[5][4];
        int southValue = map.map[5][6];
        int eastValue = map.map[6][5];
        int westValue = map.map[4][5];

        assertEquals(DijkstraMap.CARDINAL_COST, northValue, "Cardinal neighbor should have value 2");
        assertEquals(DijkstraMap.CARDINAL_COST, southValue, "Cardinal neighbor should have value 2");
        assertEquals(DijkstraMap.CARDINAL_COST, eastValue, "Cardinal neighbor should have value 2");
        assertEquals(DijkstraMap.CARDINAL_COST, westValue, "Cardinal neighbor should have value 2");
    }

    @Test
    void calculate_DiagonalNeighborCost() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                map.map[x][y] = DijkstraMap.BASE_VALUE;
            }
        }
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.calculate();

        int neValue = map.map[6][4];
        int nwValue = map.map[4][4];
        int seValue = map.map[6][6];
        int swValue = map.map[4][6];

        assertEquals(DijkstraMap.DIAGONAL_COST, neValue, "Diagonal neighbor should have value 3");
        assertEquals(DijkstraMap.DIAGONAL_COST, nwValue, "Diagonal neighbor should have value 3");
        assertEquals(DijkstraMap.DIAGONAL_COST, seValue, "Diagonal neighbor should have value 3");
        assertEquals(DijkstraMap.DIAGONAL_COST, swValue, "Diagonal neighbor should have value 3");
    }

    @Test
    void calculate_StopsAtObstacles() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.map[4][5] = DijkstraMap.OBSTACLE_VALUE;
        map.calculate();

        assertEquals(DijkstraMap.OBSTACLE_VALUE, map.map[4][5], "Obstacle should stay obstacle");
    }

    @Test
    void getMapGoalsBlocked_ConvertsGoalsToObstacles() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        map.map[5][5] = DijkstraMap.GOAL_VALUE;

        int[][] blockedMap = map.getMapGoalsBlocked();
        assertEquals(DijkstraMap.OBSTACLE_VALUE, blockedMap[5][5]);
    }

    @Test
    void getMapGoalsBlocked_PreservesOtherValues() {
        PlayerDijkstraMap map = new PlayerDijkstraMap(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                map.map[x][y] = DijkstraMap.BASE_VALUE;
            }
        }
        map.map[5][5] = DijkstraMap.GOAL_VALUE;
        map.calculate();

        int originalValue = map.map[3][3];
        int[][] blockedMap = map.getMapGoalsBlocked();
        assertEquals(originalValue, blockedMap[3][3], "getMapGoalsBlocked should preserve non-goal values");
    }

    @Test
    void baseValue_Is500() {
        assertEquals(500, DijkstraMap.BASE_VALUE);
    }

    @Test
    void obstacleValue_Is42069() {
        assertEquals(42069, DijkstraMap.OBSTACLE_VALUE);
    }

    @Test
    void goalValue_Is0() {
        assertEquals(0, DijkstraMap.GOAL_VALUE);
    }

    @Test
    void cardinalCost_Is2() {
        assertEquals(2, DijkstraMap.CARDINAL_COST);
    }

    @Test
    void diagonalCost_Is3() {
        assertEquals(3, DijkstraMap.DIAGONAL_COST);
    }
}
