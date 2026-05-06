package com.ai.jobpilotbackend.resume.mapper;

import com.ai.jobpilotbackend.resume.entity.AiTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AitaskMapper {

    void insert(AiTask task);

    AiTask findById(Long id);

    void updateResult(@Param("id") Long id,
                      @Param("result") String result,
                      @Param("status") Integer status);

    void updateStatus(@Param("id") Long id,
                      @Param("status") Integer status);
}
