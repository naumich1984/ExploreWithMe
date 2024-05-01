package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "title"})
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;
}
