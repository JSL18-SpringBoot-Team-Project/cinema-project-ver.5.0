package com.movie.mapper;

import com.movie.domain.MovieDTO;
import com.movie.domain.MovieDetails;
import com.movie.domain.Movies;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MovieDetailMapper {

    @Insert("INSERT INTO movie_details (movie_id, trailer, movie_img, country, production, distribution, director, actor, content) values (#{movieId}, #{trailer}, #{movieImg}, #{country}, #{production}, #{distribution}, #{director}, #{actor}, #{content})")
    public long insertMovieDetail(MovieDetails movieDetails);

    @Select("select a.id, a.title, a.running_time, a.release_date, a.end_date, a.audience, a.genre, a.poster_img, b.id " +
            "as detail_id, b.movie_id " +
            "as detail_movie_id, b.trailer, b.movie_img, b.country, b.production, b.distribution, b.director, b.actor, b.content " +
            "from movies a join movie_details b on a.id = b.movie_id where a.id = #{movieId}")
    @Results({
            @Result(property = "movies.id", column = "id"),
            @Result(property = "movies.title", column = "title"),
            @Result(property = "movies.runningTime", column = "running_time"),
            @Result(property = "movies.releaseDate", column = "release_date"),
            @Result(property = "movies.endDate", column = "end_date"),
            @Result(property = "movies.audience", column = "audience"),
            @Result(property = "movies.genre", column = "genre"),
            @Result(property = "movies.posterImg", column = "poster_img"),
            @Result(property = "movieDetails.detail_id", column = "detail_id"),
            @Result(property = "movieDetails.movieId", column = "detail_movie_id"),
            @Result(property = "movieDetails.trailer", column = "trailer"),
            @Result(property = "movieDetails.movieImg", column = "movie_img"),
            @Result(property = "movieDetails.country", column = "country"),
            @Result(property = "movieDetails.production", column = "production"),
            @Result(property = "movieDetails.distribution", column = "distribution"),
            @Result(property = "movieDetails.director", column = "director"),
            @Result(property = "movieDetails.actor", column = "actor"),
            @Result(property = "movieDetails.content", column = "content")
    })
    public MovieDTO getMovieDetail(long movieId);

    // 전체 영화 표시
    @Select("select * from movies")
    public List<Movies> getMovie();

    // 상영 종료인 영화
    @Select("SELECT * FROM movies WHERE end_date < NOW() ORDER BY id DESC;")
    public List<Movies> endMovie();

    // 상영 예정인 영화
    @Select("select * from movies where release_date >= now() order by id desc;")
    public List<Movies> scheduleMovie();

    // 상영 중인 영화
    @Select("select * from movies where end_date >= NOW() and release_date <= now() ORDER BY id DESC;")
    public List<Movies> startMovie();
}
