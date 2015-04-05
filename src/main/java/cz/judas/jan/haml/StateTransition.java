package cz.judas.jan.haml;

public class StateTransition {
    private final State newState;
    private final int newPosition;

    public StateTransition(State newState, int newPosition) {
        this.newState = newState;
        this.newPosition = newPosition;
    }

    public State getNewState() {
        return newState;
    }

    public int getNewPosition() {
        return newPosition;
    }
}
