package ru.practicum.ewm.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    @Column(name = "pinned")
    @Builder.Default
    private Boolean pinned = false;

    /**
     * Заголовок подборки
     */
    @Column(name = "title", nullable = false)
    @Size(min = 3, max = 255)
    private String title;

    /**
     * Список событий входящих в подборку
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;
}