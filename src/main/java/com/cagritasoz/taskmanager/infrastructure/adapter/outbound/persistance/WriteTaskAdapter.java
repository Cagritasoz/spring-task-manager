package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance;

import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.TaskEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper.TaskJpaMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WriteTaskAdapter implements WriteTaskPort {

    private final UserJpaRepository userJpaRepository;

    private final TaskJpaRepository taskJpaRepository;

    private final TaskJpaMapper taskJpaMapper;

    //Using a join fetch along with pagination can give unwanted results!

    @Override
    @Transactional
    public Task saveTask(Long userId, Task task) {

        UserEntity userEntity = userJpaRepository.getReferenceById(userId);

        TaskEntity taskEntity = taskJpaMapper.toEntityModel(task, userEntity);

        return taskJpaMapper.toDomainModel(taskJpaRepository.save(taskEntity), userId);

    }

    @Override
    @Transactional
    public Task updateTask(Long userId, Long taskId, Task task) {

        Optional<TaskEntity> taskEntityOptional = taskJpaRepository.findById(taskId);

        taskEntityOptional.ifPresent(taskEntity -> {

            taskEntity.setTitle(task.getTitle());
            taskEntity.setDescription(task.getDescription());
            taskEntity.setDueDate(task.getDueDate());

        });

        return taskEntityOptional.map(taskEntity -> taskJpaMapper.toDomainModel(taskEntity, userId))
                .orElse(null);
    }

    @Override
    public void deleteTask(Long taskId) {

        taskJpaRepository.deleteById(taskId);

    }


}


/*
-Proxy Object: A proxy object is an object that holds only the primary key value. (The proxy knows the ID immediately,
Other fields are not loaded yet.)

-It will trigger a database query to initialize its other fields only
when you call a getter method for any non-ID attribute. Then this database query runs to load
not only that field but all the fields.
After the fields are loaded if a getter is used on a field no database query is executed.
If an object field's fetch type is specified as FetchType.LAZY when this object is loaded from the database(maybe a
findBy method) this field is a proxy object that currently know the primary key of that field.
FetchType.EAGER is the opposite meaning when the object is loaded from the database this field and all of this fields
data(fields fields) are automatically loaded. This might be unnecessary.

-A proxy object is one mechanism Hibernate uses to implement lazy loading.

-If an object is loaded by using a
getReference method that object is a proxy object.

-getByReference method is used for optimization. It avoids unnecessary database queries.

-Lazy loading also works on collections. A collection proxy is lazily loaded. To actually load all the
data of the collection a get method to that collection can be called. An example of this can be a parent class
that has @OneToMany(fetch = FetchType.LAZY) on a field like List<ChildEntity> when this parent object is loaded
from the database this list is loaded lazily so it is a collection proxy. getChildren method returning this list
would then load all the children entities as well.

 */
