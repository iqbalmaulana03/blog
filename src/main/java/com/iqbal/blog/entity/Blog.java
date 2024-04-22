package com.iqbal.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blog")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title;

    private String body;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
