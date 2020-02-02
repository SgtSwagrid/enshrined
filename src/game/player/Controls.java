package game.player;

import game.input.InputHandler;

public class Controls {
    
    public final InputHandler INPUT;
    public final int MOVE_UP, MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN, SHOOT;
    
    public Controls(InputHandler input, int moveUp, int moveLeft,
            int moveRight, int moveDown, int shoot) {
        INPUT = input;
        MOVE_UP = moveUp; MOVE_LEFT = moveLeft;
        MOVE_RIGHT = moveRight; MOVE_DOWN = moveDown;
        SHOOT = shoot;
    }
}