package com.spring.one.microstarter.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private  String schoolName;

    private  String schoolAddress;

    private String oldName;

    private String newName;
}
