package com.movie.mapper;

import com.movie.domain.Role;
import com.movie.domain.SocialProvider;
import com.movie.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    // 사용자 추가 (일반 사용자)
    @Insert("INSERT INTO users (email, password, name, phone, birth) " +
            "VALUES (#{email}, #{password}, #{name}, #{phone}, #{birth})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    // 소셜 사용자 추가
    @Insert("INSERT INTO users (email, name, social_id, social_provider, role, created_at, updated_at) " +
            "VALUES (#{email}, #{name}, #{socialId}, #{socialProvider}, #{role}, #{createdAt}, #{updatedAt})")
    void insertSocialUser(User user);

    // 소셜 사용자 정보 업데이트
    @Update("UPDATE users SET social_id = #{socialId}, social_provider = #{socialProvider}, updated_at = #{updatedAt} WHERE email = #{email}")
    void updateSocialUser(User user);

    // 이메일로 사용자 검색
    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "role", column = "role", javaType = Role.class, typeHandler = com.movie.handler.EnumTypeHandler.class),
            @Result(property = "socialProvider", column = "social_provider", javaType = SocialProvider.class, typeHandler = com.movie.handler.EnumTypeHandler.class)
    })
    Optional<User> findByEmail(String email);

    // 이메일 존재 여부 확인
    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email}")
    boolean existsByEmail(String email);

    // 사용자 정보 조회 (ID 기준)
    @Select("SELECT * FROM users WHERE id = #{userId}")
    public User getUserInfo(long userId);

    // 사용자 리스트 조회
    @Select("SELECT * FROM users order by id")
    public List<User> getUserList();

    // 사용자 삭제
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

    @Select("SELECT COUNT(*) > 0 FROM users WHERE email = #{email} AND social_provider = #{provider}")
    boolean existsByEmailAndProvider(String email, String provider);

    @Update("UPDATE users SET password = #{password}, updated_at = NOW() WHERE email = #{email}")
    void updatePassword2(@Param("email") String email, @Param("password") String password);

}