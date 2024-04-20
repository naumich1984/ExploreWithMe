package ru.practicum.ewm.model;

public enum EventState {
    WAITED,
    PUBLISHED,
    REJECTED;

    static EventState from(String state) {
        if (state == null) {

            return null;
        }
        for (EventState value : EventState.values()) {
            if (value.name().equals(state)) {

                return value;
            }
        }

        return null;
    }
}
