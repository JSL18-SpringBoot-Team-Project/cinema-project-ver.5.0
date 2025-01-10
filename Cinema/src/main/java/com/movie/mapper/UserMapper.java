package com.movie.mapper;

import com.movie.domain.Role;
import com.movie.domain.SocialProvider;
import com.movie.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (email, password, name, phone, birth) " +
            "VALUES (#{email}, #{password}, #{name}, #{phone}, #{birth})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Insert("INSERT INTO users (email, name, social_id, social_provider, role, created_at, updated_at) " +
            "VALUES (#{email}, #{name}, #{socialId}, #{socialProvider}, #{role}, #{createdAt}, #{updatedAt})")
    void insertSocialUser(User user);

    @Update("UPDATE users SET social_id = #{socialId}, social_provider = #{socialProvider}, updated_at = #{updatedAt} WHERE email = #{email}")
    void updateSocialUser(User user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "role", column = "role", javaType = Role.class, typeHandler = com.movie.handler.EnumTypeHandler.class),
            @Result(property = "socialProvider", column = "social_provider", javaType = SocialProvider.class, typeHandler = com.movie.handler.EnumTypeHandler.class)
    })
    Optional<User> findByEmail(String email);

    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{userId}")
    public User getUserInfo(long userId);

    @Select("SELECT * FROM users order by id")
    public List<User> getUserList();

    @Delete("DELETE FROM users where id = #{id}")
    public long deleteUser(long id);

    // 비밀번호 조회
    @Select("SELECT password FROM users WHERE id = #{userId}")
    String getPassword(long userId);

    // 비밀번호 업데이트
    @Update("UPDATE users SET password = #{password}, last_password_change = now(), updated_at = now() WHERE id = #{id}")
    void updatePassword(User user);

    // 사용자 정보 수정
    @Update("UPDATE users SET name = #{name}, email = #{email}, phone = #{phone}, birth = #{birth}, updated_at = now() WHERE id = #{id}" +
            "AND NOT EXISTS (SELECT 1 FROM users WHERE email = #{email} AND id != #{id})")
    void updateUser(User user);


}