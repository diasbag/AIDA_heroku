package com.hackathon.mentor.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "FILES")
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String contentType;

    private Long size;

    @Lob
    private byte[] data;

    @OneToOne
    private Mentor mentor;

    @OneToOne
    private Mentee mentee;

    private String text;

}
