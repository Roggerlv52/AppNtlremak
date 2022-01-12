package com.example.netflixremak;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class MuvieActivity extends AppCompatActivity implements MovieDetailTask.MovieDtalilLoader {
    private TextView txtTitulo;
    private TextView txtDesc;
    private TextView txtCast;
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muvie);

        txtTitulo = findViewById(R.id.text_titulo_filme);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        recyclerView = findViewById(R.id.recycleview_Similar);

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }
        //Metodo para trocar imagem dinamicamente
        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);
        if (drawable != null) {
            Drawable movierCover = ContextCompat.getDrawable(this, R.drawable.movie_4
            );
            drawable.setDrawableByLayerId(R.id.coverDlawable, movierCover);
            ((ImageView) findViewById(R.id.image_couver_View)).setImageDrawable(drawable);
        }

        List<Movie> movies = new ArrayList<>();

        movieAdapter = new MovieAdapter(movies);
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");

            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDtalilLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResult(MovieDtalil movieDtalil) {
        txtTitulo.setText(movieDtalil.getMovie().getTitle());
        txtDesc.setText(movieDtalil.getMovie().getDesc());
        txtCast.setText(movieDtalil.getMovie().getCast());

        movieAdapter.setMovies(movieDtalil.getMoviesSimilar());
        movieAdapter.notifyDataSetChanged();
    }

    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover1);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private final List<Movie> movies;

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

