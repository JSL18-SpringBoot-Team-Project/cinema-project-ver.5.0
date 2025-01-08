package com.movie.mapper;

import com.movie.domain.User;

public class UserSqlProvider {
    public String insertUser(User user) {
        StringBuilder sql = new StringBuilder("insert into users (email, password");

        if(user.getName() != null) {
            sql.append(", name");
        }
        if(user.getPhone() != null) {
            sql.append(", phone");
        }
        if(user.getBirth() != null) {
            sql.append(", birth");
        }

        sql.append(") values (#{email}, #{password}");

        if(user.getName() != null) {
            sql.append(", #{name}");
        }
        if(user.getPhone() != null) {
            sql.append(", #{phone}");
        }
        if(user.getBirth() != null) {
            sql.append(", #{birth}");
        }

        sql.append(")");

        return sql.toString();
    }
}
