package com.cagritasoz.taskmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortField {

    private String property; //sort=dueDate | property is the name "dueDate".
    private boolean isAscending; //sort=dueDate,asc&sort=title,desc | asc or desc is the ascending indicated by boolean.
}
