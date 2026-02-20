package com.data.explorer.mapper;

import com.data.explorer.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM tb_user WHERE user_id = #{userId} AND status = 1")
    User findByUserId(@Param("userId") String userId);

    @Select("SELECT * FROM tb_user WHERE user_id = #{userId} AND password = #{password} AND status = 1")
    User findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);

    @Select("SELECT * FROM tb_user WHERE status = 1 ORDER BY gmt_create DESC")
    List<User> findAll();

    @Insert("INSERT INTO tb_user (status, user_id, user_name, password, creator, modifier) " +
            "VALUES (1, #{userId}, #{userName}, #{password}, #{creator}, #{modifier})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE tb_user SET status = 2, modifier = #{modifier} WHERE user_id = #{userId}")
    int disableUser(@Param("userId") String userId, @Param("modifier") String modifier);

    @Select("SELECT COUNT(*) FROM tb_user WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") String userId);
}
