package ru.practicum.ewm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.practicum.ewm.config.AppConfig;
import ru.practicum.ewm.model.dto.CustomDateDeserializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistics")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConfig.DATE_FORMAT)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime timestamp;

    @Column(name = "uri", nullable = false)
    @Size(min = 3, max = 255)
    private String uri;

    @Type(type = "jsonb")
    @Column(name = "attributes", nullable = false)
    private Attributes attributes;
}