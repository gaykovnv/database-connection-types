package com.example.adminservice.entity;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "houses_param")
public class HousesParam {

    @Id
    @Column
    private Long id;

    @Column(name = "id_object")
    private Long objectid;

    @Column(name = "kd_type")
    private Long type;

    @Column(name = "vl")
    private String value;

    @Column(name = "change_id")
    private String changeId;

    @Column(name = "change_id_end")
    private String changeIdEnd;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
