package com.example.mybestyoutube;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mybestyoutube.daodatabase.YoutubeVideoDatabase;
import com.example.mybestyoutube.pojo.YoutubeVideo;

public class YoutubeVideoDetailActivity extends AppCompatActivity {
    private TextView tvTitre;
    private TextView tvDescription;
    private TextView tvUrl;
    private TextView tvCategorie;
    private Toolbar toolbar;
    private Button btnVoir;
    private Button btnAddToFavorites;
    private YoutubeVideo youtubeVideo;
    private boolean isFavorite = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube_video_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        context=getApplicationContext();
        tvTitre = findViewById(R.id.tvDetailTitre);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvUrl = findViewById(R.id.tvDetailUrl);
        tvCategorie = findViewById(R.id.tvDetailCategorie);
        btnVoir = findViewById(R.id.btnVoir);
        btnAddToFavorites = findViewById(R.id.btnAddToFavorites);
        btnVoir.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));
        btnAddToFavorites.setBackgroundColor(getResources().getColor(R.color.toolbar_color));

        toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color_dark));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ImageView btnCopyUrl = findViewById(R.id.btnCopyUrl);

        btnCopyUrl.setOnClickListener(v -> {
            String url = tvUrl.getText().toString();
            if (!url.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied URL", url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(YoutubeVideoDetailActivity.this, "URL copiée dans le presse-papiers", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("youtubeVideo")) {
            youtubeVideo = (YoutubeVideo) intent.getSerializableExtra("youtubeVideo");
            if (youtubeVideo != null) {
                Log.d("YoutubeVideoDetail", "Video Titre: " + youtubeVideo.getTitre());
                tvTitre.setText(youtubeVideo.getTitre());
                tvDescription.setText(youtubeVideo.getDescription());
                tvUrl.setText(youtubeVideo.getUrl());
                tvCategorie.setText(youtubeVideo.getCategorie());

                isFavorite = youtubeVideo.getFavori() == 1;
                updateFavoriteButtonText();

                btnVoir.setOnClickListener(v -> {
                    String videoUrl = youtubeVideo.getUrl();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    startActivity(webIntent);
                });

                btnAddToFavorites.setOnClickListener(v -> {
                    toggleFavoriteStatus();
                });
            } else {
                Log.e("YoutubeVideoDetail", "Erreur : la video est null après désérialisation");
                Toast.makeText(this, "Erreur : Aucune donnée vidéo reçue", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Log.e("YoutubeVideoDetail", "Erreur : intent ou video null après extract");
            Toast.makeText(this, "Erreur : Aucune donnée vidéo reçue", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_youtube_video_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.deleteYoutubeVideo) {
            confirmDelete();
            return true;
        } else if (item.getItemId() == R.id.updateYoutubeVideo) {
            updateVideo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        if (youtubeVideo != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous vraiment supprimer cette vidéo ?")
                    .setPositiveButton("Oui", (dialog, which) -> deleteVideo())
                    .setNegativeButton("Non", null)
                    .show();
        } else {
            Log.e("YoutubeVideoConfirmDelete", "Erreur : Aucune donnée vidéo à supprimer");
            Toast.makeText(this, "Erreur : Aucune donnée vidéo à supprimer", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteVideo() {
        if (youtubeVideo != null) {
            YoutubeVideoDatabase.getDb(getApplicationContext()).YoutubeVideoDao().delete(youtubeVideo);
            Toast.makeText(this, "Video supprimée", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur : Aucune donnée vidéo à supprimer", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateVideo() {
        if (youtubeVideo != null) {
            Intent intent = new Intent(this, UpdateYoutubeVideoActivity.class);
            intent.putExtra("youtubeVideo", youtubeVideo);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erreur : Aucune donnée vidéo à mettre à jour", Toast.LENGTH_SHORT).show();
        }
    }


    private void toggleFavoriteStatus() {
        if (youtubeVideo != null) {
            isFavorite = !isFavorite;
            youtubeVideo.setFavori(isFavorite ? 1 : 0);
            YoutubeVideoDatabase.getDb(getApplicationContext()).YoutubeVideoDao().update(youtubeVideo);
            Toast.makeText(this, isFavorite ? "Ajouté aux favoris" : "Retiré des favoris", Toast.LENGTH_SHORT).show();
            updateFavoriteButtonText();
        }
    }

    private void updateFavoriteButtonText() {
        if (isFavorite) {
            btnAddToFavorites.setText("Retirer des favoris");
        } else {
            btnAddToFavorites.setText("Ajouter aux favoris");
        }
    }
}
