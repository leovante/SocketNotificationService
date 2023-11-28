package com.nlmk.adp.db.entity;

import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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