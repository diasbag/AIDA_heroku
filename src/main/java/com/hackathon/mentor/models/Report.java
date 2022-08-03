package com.hackathon.mentor.models;

import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;

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
    private DateTime reportDate;

    @Column(name = "ignore")
    private Boolean ignore;

}
