package com.data.explorer.controller;

import com.data.explorer.common.Result;
import com.data.explorer.entity.ExplorerTask;
import com.data.explorer.service.ExplorerTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class ExplorerTaskController {
    @Autowired
    private final ExplorerTaskService taskService;
    
    @PostMapping("/create")
    public Result<ExplorerTask> createTask(@RequestBody ExplorerTask task) {
        return Result.success(taskService.createTask(task));
    }
    
    @GetMapping("/list")
    public Result<List<ExplorerTask>> getTasks() {
        return Result.success(taskService.getAllTasks());
    }
}
