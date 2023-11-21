package com.nlmk.adp.db.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ActiveWebSocketUser.
 */
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