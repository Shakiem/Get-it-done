package com.shakiemsaunders.get_it_done;

public enum Priority {
    VERY_BIG_DEAL("VERY BIG DEAL"),
    BIG_DEAL("BIG DEAL"),
    NOT_A_BIG_DEAL("NOT A BIG DEAL");

    private final String name;

    Priority(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public static Priority byValue(final int value) {
        Priority returnValue = NOT_A_BIG_DEAL;
        for (final Priority priority : Priority.values()) {
            if (priority.ordinal() == value) {
                returnValue = priority;
                break;
            }
        }
        return returnValue;
    }
}
