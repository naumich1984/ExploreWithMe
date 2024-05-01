package ru.practicum.ewm.model._enum;

public enum RequestStatus {
    PENDING,
    REJECTED,
    CONFIRMED,
    CANCELED;

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
