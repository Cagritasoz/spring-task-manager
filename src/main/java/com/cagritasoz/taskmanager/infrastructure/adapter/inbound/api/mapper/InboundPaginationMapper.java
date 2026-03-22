package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.SortField;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InboundPaginationMapper {

    public <T> Pagination<T> fromPageableToPagination(Pageable pageable) {

        List<SortField> sortFields = new ArrayList<>();

        Sort sort = pageable.getSort(); //Sort never null. Even if sorting is not specified in the URL.

        if(sort.isSorted()) { //there exists at least one sorting order.

            for(Sort.Order order : sort) { //sort implements Iterable. Sort object is just a collection of Sort.Order objects.

                sortFields.add(
                        new SortField(order.getProperty(),
                                order.isAscending()));

            }

        }

        return new Pagination<>(null,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sortFields,
                0,
                0);

    }

}
