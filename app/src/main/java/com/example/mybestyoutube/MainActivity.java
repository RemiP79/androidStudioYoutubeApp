package com.example.mybestyoutube;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestyoutube.adapter.YoutubeVideoAdapter;
import com.example.mybestyoutube.daodatabase.YoutubeVideoDatabase;
import com.example.mybestyoutube.pojo.YoutubeVideo;



import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvYoutubeVideos;
    private Toolbar toolbar;
    private Context context;
    private boolean showFavoritesOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        context = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbar_color));

        rvYoutubeVideos=findViewById(R.id.rvYoutubeVideo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvYoutubeVideos.setHasFixedSize(true);
        rvYoutubeVideos.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadVideos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cheat) {
            Intent intent = new Intent(getApplicationContext(), AddYoutubeActivityLayout.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.filter_favorites) {
            showFavoritesOnly = !showFavoritesOnly;
            loadVideos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadVideos() {
        TodoAsyncTask todoAsyncTask = new TodoAsyncTask(showFavoritesOnly);
        todoAsyncTask.execute();
    }

    public class TodoAsyncTask extends AsyncTask<Void, Void, List<YoutubeVideo>> {
        private boolean showFavoritesOnly;
        public TodoAsyncTask(boolean showFavoritesOnly) {
            this.showFavoritesOnly = showFavoritesOnly;
        }

        @Override
        protected List<YoutubeVideo> doInBackground(Void... voids) {
            if (showFavoritesOnly) {
                return YoutubeVideoDatabase.getDb(context).YoutubeVideoDao().getFavoris();
            } else {
                return YoutubeVideoDatabase.getDb(context).YoutubeVideoDao().list();
            }
        }

        @Override
        protected void onPostExecute(List<YoutubeVideo> youtubeVideos) {
            super.onPostExecute(youtubeVideos);
            YoutubeVideoAdapter youtubeVideoAdapter = new YoutubeVideoAdapter(youtubeVideos, new YoutubeVideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(YoutubeVideo video) {
                    Intent intent = new Intent(MainActivity.this, YoutubeVideoDetailActivity.class);
                    intent.putExtra("youtubeVideo", video);
                    Log.d("MainActivity", "Catégorie de la vidéo : " + video.getCategorie());
                    startActivity(intent);
                }
            });
            rvYoutubeVideos.setAdapter(youtubeVideoAdapter);
        }
    }
}