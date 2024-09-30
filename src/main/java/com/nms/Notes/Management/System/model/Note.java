package com.nms.Notes.Management.System.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "note")
@Data
public class Note {
    @Id
    private String id;
    private String title;
    private String category;
    private Date created_at;
    private Date updated_at;

    private String userId;


    private String image;
    private String download;
    private String url;

}
