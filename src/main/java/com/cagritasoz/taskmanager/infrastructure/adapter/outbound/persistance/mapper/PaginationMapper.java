package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.SortField;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaginationMapper {

    public <T> Pageable fromPaginationToPageable(Pagination<T> pagination) {

        Sort sort; //Empty Collection

        if(!pagination.getSortFields().isEmpty()) { //There exists sort orders.

            List<Sort.Order> orders = new ArrayList<>();

            for(SortField sortField : pagination.getSortFields()) {

                Sort.Order order = sortField.isAscending()
                        ? Sort.Order.asc(sortField.getProperty())
                        : Sort.Order.desc(sortField.getProperty());

                orders.add(order);

            }

            sort = Sort.by(orders);

            return PageRequest.of(pagination.getPageNumber(),
                    pagination.getSize(),
                    sort);

        }

        return PageRequest.of(pagination.getPageNumber(),
                pagination.getSize());
    }


}
