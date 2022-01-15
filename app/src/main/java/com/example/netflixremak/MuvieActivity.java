package com.example.netflixremak;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflixremak.model.Movie;
import com.example.netflixremak.model.MovieDtalil;
import com.example.netflixremak.util.ImagemDowloaderTesk;
import com.example.netflixremak.util.MovieDetailTask;

import java.util.ArrayList;
import java.util.List;

public class MuvieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetalilLoader {
    private TextView txtTitulo;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muvie);

        txtTitulo = findViewById(R.id.text_titulo_filme);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        recyclerView = findViewById(R.id.recycleview_Similar);
        imgCover = findViewById(R.id.image_couver_View);

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }
        //Metodo para trocar imagem dinamicamente
       // LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        List<Movie> movies = new ArrayList<>();

        movieAdapter = new MovieAdapter(movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

                int id = extras.getInt("id");
                MovieDetailTask movieDetailTask = new MovieDetailTask(this);
                movieDetailTask.setMovieDetalilLoader(this);
                movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);

        }else {
            Toast.makeText(MuvieActivity.this, "Verifique sua conexão", Toast.LENGTH_SHORT).show();
        }
    }
            // Metodo para usar seta para voltar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
          if (item.getItemId() == android.R.id.home)
              finish();
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResult(MovieDtalil movieDtalil) {

        txtTitulo.setText(movieDtalil.getMovie().getTitle());
        txtDesc.setText(movieDtalil.getMovie().getDesc());
        txtCast.setText(movieDtalil.getMovie().getCast());
        ImagemDowloaderTesk imagemDowloaderTesk = new ImagemDowloaderTesk(imgCover);
        imagemDowloaderTesk.setShadowEnabled(true);
        imagemDowloaderTesk.execute(movieDtalil.getMovie().getCoverUrl());

        movieAdapter.setMovies(movieDtalil.getMoviesSimilar());
        movieAdapter.notifyDataSetChanged();
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover2);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        public void setMovies(List<Movie> movies) {
            this.movies.clear();
            this.movies.addAll(movies);
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater()
                    .inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImagemDowloaderTesk(holder.imageViewCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}

