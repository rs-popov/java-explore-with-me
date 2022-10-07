package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Заголовок
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Полное описание события
     */
    @Column(name = "description")
    private String description;

    /**
     * Краткое описание
     */
    @Column(name = "annotation", nullable = false)
    private String annotation;

    /**
     * Категория
     */
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    /**
     * Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Column(name = "event_date", columnDefinition = "TIMESTAMP")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;

    /**
     * Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Column(name = "created_on")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Column(name = "published_on")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    /**
     * Широта и долгота места проведения события
     */
    //@ManyToOne(cascade = {CascadeType.PERSIST})
    @ManyToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    /**
     * Нужно ли оплачивать участие
     */
    @Column(name = "paid")
    private Boolean paid;

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     */
    @Column(name = "participant_limit")
    private Long participantLimit;

    /**
     * Пользователь (краткая информация)
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    /**
     * Нужна ли пре-модерация заявок на участие
     */
    @Column(name = "request_moderation")
    @Builder.Default
    private Boolean requestModeration = true;

    /**
     * Список состояний жизненного цикла события
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    @Builder.Default
    private EventState state = EventState.PENDING;
}