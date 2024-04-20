package ru.practicum.ewm.model;

public enum RequestStatus {
    PENDING,
    CANCEL,
    CONFIRMED;

    static RequestStatus from(String status) {
        if (status == null) {

            return null;
        }
        for (RequestStatus value : RequestStatus.values()) {
            if (value.name().equals(status)) {

                return value;
            }
        }

        return null;
    }
}
