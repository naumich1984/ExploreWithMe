package ru.practicum.ewm.model._enum;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

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
