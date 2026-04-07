package com.example.todolist.mapper;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskUpdateDto;
import com.example.todolist.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateDto dto);

    TaskResponseDto toResponseDto(Task task);

    List<TaskResponseDto> toResponseDtoList(List<Task> tasks);

    Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);
}
