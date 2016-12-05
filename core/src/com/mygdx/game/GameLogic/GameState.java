package com.mygdx.game.GameLogic;

import com.badlogic.gdx.Screen;

/* Important part of game logic. With GameState we can change levels:
*  from Main Menu to N level, from N level to settings and others...*/
public abstract class GameState implements Screen{

    public abstract void handleInput(float dt);
    public abstract void update(float dt);

}
