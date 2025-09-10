package com.example.backend.feature.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "api_log", schema = "prj5")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Column(name = "model_nm", nullable = false, length = 100)
    private String modelNm;

    @Column(name = "model_ver", length = 50)
    private String modelVer;

    @Lob
    @Column(name = "prompt", nullable = false)
    private String prompt;

    @Lob
    @Column(name = "req_body")
    private String reqBody;

    @Lob
    @Column(name = "req_hdr")
    private String reqHdr;

    @Lob
    @Column(name = "res_body")
    private String resBody;

    @Lob
    @Column(name = "res_hdr")
    private String resHdr;

    @Column(name = "stat_cd")
    private Integer statCd;

    @Lob
    @Column(name = "err_msg")
    private String errMsg;

    @Column(name = "req_dttm", nullable = false)
    private LocalDateTime reqDttm;

    @Column(name = "res_dttm", nullable = false)
    private LocalDateTime resDttm;

    @Column(name = "latency_ms")
    private Integer latencyMs;

    @Column(name = "usr_id", length = 100)
    private String usrId;

}