package com.ai.jobpilotbackend.resume.mapper;

import com.ai.jobpilotbackend.resume.entity.Resume;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ResumeMapper {
    int insert(Resume resume);
    Resume findById(Long id);
    void updateParseStatus(Long id, int status);
}
