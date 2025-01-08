package com.movie.mapper;

import com.movie.domain.MovieDTO;
import com.movie.domain.MovieDetails;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MovieDetailMapper {

    @Insert("INSERT INTO movie_details (movie_id, trailer, movie_img) values (#{movieId}, #{trailer}, #{movieImg})")
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
}
