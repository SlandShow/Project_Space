package com.mygdx.game.GameLogic;

import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.GameMenu;
import com.mygdx.game.Screens.MenuPauseScene;
import com.mygdx.game.Screens.PlayScreen;

import java.util.ArrayList;

/* It`s GameState manager, witch change states */
public class GameLoader {

    private ArrayList<GameState> gameLvlList;
    public static final int LVL_1 = 2;
    public static final int MENU_PAUSE_STATE = 1;
    public static final int MENU_STATE = 0;
    public static int currentIndex;
    public static MyGdxGame game;
    public static GameLoader gameLoader;

    public GameLoader(MyGdxGame game) {
        gameLoader = this;
        this.game = game;
        this.gameLvlList = new ArrayList<GameState>();
        gameLvlList.add(new GameMenu()); // base
        gameLvlList.add(new MenuPauseScene()); // base
        currentIndex = MENU_STATE;
    }

    public void addState(GameState lvl) {
        gameLvlList.add(lvl);
    }

    public GameState getCurrentState() {
        return gameLvlList.get(currentIndex);
    }


    // TODO: OPTIMIZE THIS STUFF!
    public void setNewState() {
        if (gameLvlList.size() > 0) {

        }
        game.setScreen(gameLvlList.get(currentIndex));
    }


}
