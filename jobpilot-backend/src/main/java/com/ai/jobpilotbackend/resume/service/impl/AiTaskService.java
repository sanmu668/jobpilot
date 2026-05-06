package com.ai.jobpilotbackend.resume.service.impl;

import com.ai.jobpilotbackend.resume.entity.AiTask;
import com.ai.jobpilotbackend.resume.mapper.AitaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wzs
 * @date 2026年05月06日 0:38
 */
@Service
public class AiTaskService {
    @Autowired
    private AitaskMapper taskMapper;

    @Autowired
    private AiAsyncService asyncService;

    public Long submitTask(Long userId,Long resumeId,String jobDesc){
        AiTask task = new AiTask();
        task.setUserId(userId);
        task.setResumeId(resumeId);
        task.setJobDesc(jobDesc);
        task.setStatus(0);
        taskMapper.insert(task);

        // 异步处理任务
        asyncService.process(task.getId());
        return task.getId();
    }
}
