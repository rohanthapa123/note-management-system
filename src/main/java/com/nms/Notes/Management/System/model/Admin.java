package com.nms.Notes.Management.System.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "admin")
@Data
public class Admin {
    @Id
    private String id;
    private String full_name;
    private String email;
    private String password;
    private Date created_at;
    private Date updated_at;
}
