package com.example.mybestyoutube.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class YoutubeVideo  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    Long id;

    @ColumnInfo(name="titre")
    String titre;

    @ColumnInfo(name="description")
    String description;

    @ColumnInfo(name="url")
    String url;

    @ColumnInfo(name="categorie")
    String categorie;

    @ColumnInfo(name="favori")
    int favori;

    public YoutubeVideo(String titre, String description, String url, String categorie, int favori) {
        this.titre = titre;
        this.description = description;
        this.url = url;
        this.categorie = categorie;
        this.favori = favori;
    }

    public YoutubeVideo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getFavori() {
        return favori;
    }

    public void setFavori(int favori) {
        this.favori = favori;
    }
}
