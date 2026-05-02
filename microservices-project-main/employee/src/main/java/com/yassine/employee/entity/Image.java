package com.yassine.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// entities/Image.java
@Entity
@Builder //qui permet de cree des objets complexe
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;

    private String name;
    private String type;

    @Column(name = "IMAGE", length = 4048576)
    @Lob //binary large object
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Employee employee;
}
