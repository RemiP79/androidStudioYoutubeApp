package com.example.mybestyoutube.daodatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mybestyoutube.pojo.YoutubeVideo;

import java.util.List;

@Dao
public interface YoutubeVideoDao {

    @Query("SELECT * FROM YoutubeVideo WHERE id = :id")
    public YoutubeVideo find(Long id);

    @Query("SELECT * FROM YoutubeVideo")
    public List<YoutubeVideo> list();

    @Insert
    public void add(YoutubeVideo...YoutubeVideos);

    @Update
    public void update(YoutubeVideo...YoutubeVideos);

    @Delete
    public void delete(YoutubeVideo...YoutubeVideos);

    @Query("SELECT * FROM YoutubeVideo WHERE favori = 1")
    List<YoutubeVideo> getFavoris();
}
