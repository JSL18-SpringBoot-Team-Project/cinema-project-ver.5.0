package com.movie.mapper;

import com.movie.domain.ScreenScheduleSeatDto;
import com.movie.domain.Seats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeatMapper {

    public long insertSeat(List<Seats> seats);

    public ScreenScheduleSeatDto getSeat(long scheduleId);

    @Select("SELECT * FROM seats WHERE id = #{id}")
    public Seats checkSeat(long id);

    @Update("UPDATE seats SET state = 2 WHERE id = #{id}")
    public long bookingStates(long id);

    @Update("UPDATE seats SET state = 1 WHERE id = #{id}")
    public long paymentStates(long id);

    @Update("UPDATE seats SET state = 0 WHERE id = #{id}")
    public long resetStates(long id);

    @Select("SELECT id, screen_id, schedule_id, seat_column, seat_row, seat_price, state " +
            "FROM seats WHERE schedule_id = #{scheduleId}")
    List<Seats> getSeatsByBookingId(@Param("scheduleId") Long scheduleId);

    public ScreenScheduleSeatDto getScreenScheduleByScheduleId(long scheduleId);
}
