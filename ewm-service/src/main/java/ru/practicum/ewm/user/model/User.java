package ru.practicum.ewm.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Имя
     */
    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 255)
    private String name;

    /**
     * Почтовый адрес
     */
    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;
}