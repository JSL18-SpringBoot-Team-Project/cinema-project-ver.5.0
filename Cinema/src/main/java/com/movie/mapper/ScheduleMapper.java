package com.movie.mapper;

import com.movie.domain.Schedules;
import com.movie.domain.ScreenScheduleSeatDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    public long insertSchedule(Schedules schedules);

    public List<Schedules> usedScheduleList();

    public List<Schedules> scheduleList();

    public List<ScreenScheduleSeatDto> movieScheduleList(long movieId);

    @Select("SELECT * FROM schedules WHERE id = #{id}")
    public Schedules getSchedule(long id);

}
