package com.raf.usermanagement.requests;

import lombok.Data;

import java.util.Date;

@Data
public class ScheduleRequest {
    private Long id;
    private Date date;
    private String action;
}
