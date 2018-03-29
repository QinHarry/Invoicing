package com.halen.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "t_log")
public class Log {

    public final static String LOGIN_ACTION = "LoginAction";
    public final static String LOGOUT_ACTION = "LogoutAction";
    public final static String QUERY_ACTION = "QueryAction";
    public final static String UPDATE_ACTION = "UpdateAction";
    public final static String ADD_ACTION = "AddAction";
    public final static String DELETE_ACTION = "DeleteAction";

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 1000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Transient
    private Date btime;

    @Transient
    private Date etime;

    @Column(length = 100)
    private String type;

    @ManyToOne
    private User user;

    public Log(String type, String content) {
        super();
        this.type = type;
        this.content = content;
    }
}
