package com.shakiemsaunders.get_it_done;

public enum State {
    INCOMPLETE,
    COMPLETE;

    public static State byValue(final int value) {
        State returnValue = INCOMPLETE;
        for (final State state : State.values()) {
            if (state.ordinal() == value) {
                returnValue = state;
                break;
            }
        }
        return returnValue;
    }
}
