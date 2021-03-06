package com.lukka.notifybackend.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "statistics")
public class Statistics {
    @Id
    private int id;

    @NotNull
    private int visitorCount;
}

