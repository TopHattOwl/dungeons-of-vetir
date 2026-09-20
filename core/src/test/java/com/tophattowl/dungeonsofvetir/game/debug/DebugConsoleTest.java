package com.tophattowl.dungeonsofvetir.game.debug;

import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleActiveChangedEvent;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DebugConsoleTest {

    private DebugConsole console;

    @BeforeEach
    void setUp() {
        console = new DebugConsole();
    }

    @AfterEach
    void tearDown() {
        console.dispose();
    }

    @Test
    void multiLineOutputIsSplitIntoIndividualLines() {
        console.submit("help");

        assertTrue(console.getOutputLines().contains("> help"));
        for (String line : console.getOutputLines()) {
            assertFalse(line.contains("\n"), "output entry contains a newline: " + line);
        }
    }

    @Test
    void outputBufferIsCapped() {
        for (int i = 0; i < 200; i++) {
            console.submit("help");
        }
        assertTrue(console.getOutputLines().size() <= 300);
    }

    @Test
    void historyNavigatesBackwardsAndForwards() {
        console.submit("help");
        console.submit("clear");

        assertEquals("clear", console.navigateHistory(-1, ""));
        assertEquals("help", console.navigateHistory(-1, ""));
        assertEquals("clear", console.navigateHistory(1, ""));
        assertEquals("", console.navigateHistory(1, ""));
    }

    @Test
    void historyRestoresDraft() {
        console.submit("help");

        assertEquals("help", console.navigateHistory(-1, "work in progress"));
        assertEquals("work in progress", console.navigateHistory(1, "ignored-current"));
    }

    @Test
    void historyWithNoEntriesReturnsNull() {
        assertNull(console.navigateHistory(-1, ""));
    }

    @Test
    void submitSwallowsCommandExceptions() {
        assertDoesNotThrow(() -> console.submit("entity_info")); // no args -> NumberFormatException

        assertTrue(console.getOutputLines().stream().anyMatch(l -> l.startsWith("Error:")),
            "expected an error line in the output");
    }

    @Test
    void exitDeactivatesAndEmitsActiveChanged() {
        AtomicBoolean lastActive = new AtomicBoolean(false);
        EventBus.ListenerHandle<ConsoleActiveChangedEvent> handle =
            EventBus.on(ConsoleActiveChangedEvent.class, e -> lastActive.set(e.active()));

        console.setActive(true);
        assertTrue(console.isActive());

        lastActive.set(false);
        console.submit("exit");

        assertFalse(console.isActive());
        assertFalse(lastActive.get(), "deactivating should emit ConsoleActiveChangedEvent(false)");

        EventBus.off(handle);
    }

    @Test
    void dijkstraCommandDrivesOverlayControl() {
        AtomicInteger toggles = new AtomicInteger();
        DijkstraOverlayControl control = new DijkstraOverlayControl() {
            DijkstraMapType type = DijkstraMapType.PLAYER;
            boolean enabled = false;

            @Override public void setMapType(DijkstraMapType mapType) { this.type = mapType; }
            @Override public DijkstraMapType getMapType() { return type; }
            @Override public boolean isEnabled() { return enabled; }
            @Override public void toggle() { enabled = !enabled; toggles.incrementAndGet(); }
        };
        console.setDijkstraOverlayControl(control);

        console.submit("dijkstra");
        assertEquals(1, toggles.get());

        console.submit("dijkstra FACTION_MONSTER");
        assertEquals(2, toggles.get());
        assertEquals(DijkstraMapType.FACTION_MONSTER, control.getMapType());
    }
}
