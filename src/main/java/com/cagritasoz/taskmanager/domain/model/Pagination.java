package com.cagritasoz.taskmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Pagination<T> {

    private List<T> content; //Page

    int pageNumber; //Page, Pageable

    int size; //Page, Pageable

    List<SortField> sortFields; //Pageable

    long totalElements; //Page

    long totalPages;//Page



}
