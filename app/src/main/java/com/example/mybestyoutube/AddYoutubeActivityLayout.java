package com.example.mybestyoutube;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mybestyoutube.daodatabase.YoutubeVideoDatabase;
import com.example.mybestyoutube.pojo.YoutubeVideo;

public class AddYoutubeActivityLayout extends AppCompatActivity {
    private EditText etTitre;
    private EditText etDescription;
    private EditText etUrl;
    private Spinner sCategorie;
    private Button btnAjouter;
    private Button btnAnnuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_youtube_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTitre = findViewById(R.id.etTitre);
        etDescription = findViewById(R.id.etDescription);
        etUrl = findViewById(R.id.etUrl);
        sCategorie=findViewById(R.id.sCategorie);
        btnAjouter = findViewById(R.id.btnAjouter);
        btnAnnuler = findViewById(R.id.btnAnnuler);

        Spinner spinner = (Spinner) findViewById(R.id.sCategorie);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.categorie,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbarGoBack);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnAjouter.setOnClickListener(v -> {handleAjouterButtonClick();});
        btnAjouter.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));
        btnAnnuler.setBackgroundColor(getResources().getColor(R.color.delete_color));
        btnAnnuler.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleAjouterButtonClick(){
        String titre = etTitre.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String url = etUrl.getText().toString().trim();
        String categorie = sCategorie.getSelectedItem().toString().trim();
        int favoriDefault = 0;

        if (titre.isEmpty() || description.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
        } else {
            YoutubeVideo newYoutubeVideo =  new YoutubeVideo();
            newYoutubeVideo.setTitre(etTitre.getText().toString());
            newYoutubeVideo.setDescription(etDescription.getText().toString());
            newYoutubeVideo.setUrl(etUrl.getText().toString());
            newYoutubeVideo.setCategorie(categorie);
            newYoutubeVideo.setFavori(favoriDefault);

            YoutubeVideoDatabase.getDb(getApplicationContext()).YoutubeVideoDao().add(newYoutubeVideo);
            Toast.makeText(this, "Vidéo ajoutée avec succès.", Toast.LENGTH_SHORT).show();
            finish();
            }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}