package ru.practicum.ewm.model.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"lat", "lon"})
public class LocationDto {

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;
}
