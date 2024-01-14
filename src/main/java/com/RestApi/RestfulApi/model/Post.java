package com.RestApi.RestfulApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "post")

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
//TIME STAMP
    @Column(columnDefinition = "DATE")
   // @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDate postDate;

    private String users;

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
        //this.user = user;
    }
}
