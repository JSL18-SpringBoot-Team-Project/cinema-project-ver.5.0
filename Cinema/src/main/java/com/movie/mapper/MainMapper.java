package com.movie.mapper;

import com.movie.domain.Events;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {

    public List<Events> mainevent();
}
