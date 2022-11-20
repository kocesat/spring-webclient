package com.kocesat.webclient.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Todo implements Serializable {
    private static final long serialVersionUID  = -1L;

    private Integer id;
    private Integer userId;
    private String title;
    private boolean completed;
}
