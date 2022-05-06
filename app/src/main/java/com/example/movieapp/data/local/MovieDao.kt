package com.example.movieapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.model.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_entity")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Remplaza el registro que ya contenga un id igual
    suspend fun saveMovie(movie: MovieEntity)

}