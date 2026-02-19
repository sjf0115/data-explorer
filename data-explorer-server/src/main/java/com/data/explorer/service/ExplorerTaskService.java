package com.data.explorer.service;

import com.data.explorer.entity.ExplorerTask;
import com.data.explorer.mapper.ExplorerTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExplorerTaskService {
    
    private final ExplorerTaskMapper taskMapper;
    
    public ExplorerTask createTask(ExplorerTask task) {
        task.setStatus("PENDING");
        taskMapper.insert(task);
        return task;
    }
    
    public List<ExplorerTask> getAllTasks() {
        return taskMapper.findAll();
    }
}
