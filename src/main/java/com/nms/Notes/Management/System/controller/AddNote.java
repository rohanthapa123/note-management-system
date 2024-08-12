package com.nms.Notes.Management.System.controller;

import lombok.Data;

import java.util.Date;

@Data
public class AddNote {

    private String title;
    private String category;
    private String user_id;
    private String image;
}
