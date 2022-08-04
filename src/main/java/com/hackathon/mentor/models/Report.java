package com.hackathon.mentor.models;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_table")
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reporter")
    private Long reporterID;

    @Column(name = "harasser")
    private Long harasserID;

    @Column(name = "reason")
    private String reason;
    @Column(name = "report_date")
    private Date reportDate;

    @Column(name = "ignore")
    private Boolean ignore;
    @OneToMany
    private List<Image> images;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
