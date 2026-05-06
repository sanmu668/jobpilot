package com.ai.jobpilotbackend.resume.mapper;

import com.ai.jobpilotbackend.resume.entity.ResumeDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ResumeDetailMapper {
    @Insert("""
        insert into resume_detail(resume_id, name, phone, email, skills, education, experience, skill_tags, parse_status, created_at, updated_at)
        values 
        (#{resumeId}, #{name}, #{phone}, #{email}, #{skills}, #{education}, #{experience}, #{skillTags}, #{parseStatus}, now(), now())
        """)
    void insert(ResumeDetail resumeDetail);

    @Select("""
    select * from resume_detail where resume_id = #{resumeId}
    """)
    ResumeDetail findByResumeId(Long resumeId);

    @Update("""
    update resume_detail set  name = #{name}, phone = #{phone}, email = #{email}, skills = #{skills}, education = #{education}, experience = #{experience}, skill_tags = #{skillTags}, parse_status = #{parseStatus}, updated_at = now() where resume_id = #{resumeId}
""")
    void update(ResumeDetail resumeDetail,Long resumeId);
}
