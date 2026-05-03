package com.ai.jobpilotbackend.user.mapper;

import com.ai.jobpilotbackend.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author wzs
 * @date 2026年05月03日 16:42
 */
@Mapper
public interface UserMapper {
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);

    @Insert("""
        insert into user (email, password, username,  role,   status, created_at )
        values (#{email}, #{password}, #{username}, #{role}, #{status}, now())
""")
    int insert(User user);
}
