package net.bughandlers.bugtracker.model;


import com.mongodb.client.model.Collation;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "bug")
@Data
@NoArgsConstructor
public class Bug {

    @Field("_id")
    @Id
    private ObjectId ID ;
    private String Bug_Title;
    private String Operating_System;
    private String Priority;
    private Date Start_Date;
    private Date End_Date;
    private String Status;
    private String Assignee;
    private int Bug_Hours;
    private String Description;
    private LocalDateTime Date;
}
