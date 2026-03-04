package com.tophattowl.dungeonsofvetir;

import com.badlogic.gdx.Game;
import com.tophattowl.dungeonsofvetir.display.screens.GameScreen;


public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
