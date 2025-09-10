package com.example.backend.feature.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "image", schema = "prj5")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false, length = 36)
    private String uuid;

    @Column(name = "s3_key", nullable = false, length = 512)
    private String s3Key;

    @Column(name = "s3_url", nullable = false, length = 1024)
    private String s3Url;

    @ColumnDefault("current_timestamp()")
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "meta")
    private String meta;

}