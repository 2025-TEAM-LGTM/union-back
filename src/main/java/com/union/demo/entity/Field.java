package com.union.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name="field")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Field {
    @Id
    @Column(name="field_id")
    private Integer fieldId;

    @Column(name="field_name", nullable = false)
    private String fieldName;
}
