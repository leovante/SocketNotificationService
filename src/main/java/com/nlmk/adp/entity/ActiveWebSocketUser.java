package com.nlmk.adp.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActiveWebSocketUser {

    @Id
    private String id;

    private String username;

    private Calendar connectionTime;

}