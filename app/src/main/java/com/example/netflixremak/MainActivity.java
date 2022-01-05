package com.example.netflixremak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.netflixremak.model.Categori;
import com.example.netflixremak.model.Movie;
import com.example.netflixremak.util.CategoryTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {
    private MainAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.Recyclerview_main);
        List<Categori> categoris = new ArrayList<>();

        /*
          Dados fake
        for (int j = 0; j < 10; j++) {
            Categori category = new Categori();
            category.setName("Categoria" + j);
            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                Movie movie = new Movie();
             //   movie.setCoverUrl(R.drawable.movie);
                movies.add(movie);
            }
            category.setMovies(movies);
            categoris.add(category);
        }
       */

         mainAdapter = new MainAdapter(categoris);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mainAdapter);

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @Override
    public void onResult(List<Categori> categoris) {
        mainAdapter.setCategory(categoris);
        mainAdapter.notifyDataSetChanged();
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView ImageUrl;
        public MovieHolder(@NonNull View itemView) {
            super(itemView); // -> itemView que vem do conteiner principal muve_item pra contruir 
            // quem constroi é o Adapter
            ImageUrl = itemView.findViewById(R.id.image_view_cover1);
        }
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        RecyclerView recyclerViewMovie;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.text_view_titulo);
            recyclerViewMovie = itemView.findViewById(R.id.recycleview_Move);

        }
    }

    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {
        private List<Categori> categoris;

        private MainAdapter(List<Categori> categoris) {
            this.categoris = categoris;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Categori category = categoris.get(position);
            holder.textViewTitulo.setText(category.getName());
            holder.recyclerViewMovie.setAdapter(new MovieAdapter(category.getMovies()));
            holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));

        }

        @Override
        public int getItemCount() {
            return categoris.size();
        }

         void setCategory(List<Categori> categoris) {
            this.categoris.clear();
            this.categoris.addAll(categoris);

        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private final List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.muve_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            // holder.ImageUrl.setImageResource(movie.getCoverUrl());

        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}