package ru.practicum.ewm.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attributes implements Serializable {
    private String app;
    private String ip;

}
