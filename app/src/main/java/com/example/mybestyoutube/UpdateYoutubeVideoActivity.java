package com.example.mybestyoutube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybestyoutube.daodatabase.YoutubeVideoDatabase;
import com.example.mybestyoutube.pojo.YoutubeVideo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mybestyoutube.pojo.YoutubeVideo;

public class UpdateYoutubeVideoActivity extends AppCompatActivity {
    private EditText etUpdateTitre;
    private EditText etUpdateDescription;
    private EditText etUpdateUrl;
    private Spinner sUpdateCategorie;
    private Button btnUpdate;
    private YoutubeVideo youtubeVideo;
    private TextView tvTitreLabel;
    private TextView tvDescriptionUpdateLabel;
    private TextView tvUrlUpdateLabel;
    private TextView tvCategorieUpdateLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_youtube_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etUpdateTitre = findViewById(R.id.etUpdateTitre);
        etUpdateDescription = findViewById(R.id.etUpdateDescription);
        etUpdateUrl = findViewById(R.id.etUpdateUrl);
        btnUpdate = findViewById(R.id.btnUpdate);
        tvTitreLabel = findViewById(R.id.tvTitreLabel);
        tvDescriptionUpdateLabel = findViewById(R.id.tvDescriptionUpdateLabel);
        tvUrlUpdateLabel = findViewById(R.id.tvUrlUpdateLabel);
        tvCategorieUpdateLabel = findViewById(R.id.tvCategorieUpdateLabel);

        btnUpdate.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));

        sUpdateCategorie = (Spinner) findViewById(R.id.sUpdateCategorie);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorie,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sUpdateCategorie.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbarUpdateGoBack);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Intent
        youtubeVideo = (YoutubeVideo) getIntent().getSerializableExtra("youtubeVideo");
        if (youtubeVideo != null) {
            etUpdateTitre.setText(youtubeVideo.getTitre());
            etUpdateDescription.setText(youtubeVideo.getDescription());
            etUpdateUrl.setText(youtubeVideo.getUrl());
            sUpdateCategorie.getSelectedItem();
        } else {
            Toast.makeText(this, "Erreur dans la mise à jour de la video", Toast.LENGTH_SHORT).show();
            finish();
        }
        btnUpdate.setOnClickListener(v -> {
            updateVideo();
        });
    }
    private void updateVideo() {
        if (youtubeVideo != null) {
            youtubeVideo.setTitre(etUpdateTitre.getText().toString());
            youtubeVideo.setDescription(etUpdateDescription.getText().toString());
            youtubeVideo.setUrl(etUpdateUrl.getText().toString());
            youtubeVideo.setCategorie(sUpdateCategorie.getSelectedItem().toString());

            YoutubeVideoDatabase.getDb(getApplicationContext()).YoutubeVideoDao().update(youtubeVideo);

            Toast.makeText(this, "Video mise à jour avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur dans la mise à jour de la vidéo", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}