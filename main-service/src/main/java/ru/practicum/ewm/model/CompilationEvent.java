package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilations_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompilationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_e_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
