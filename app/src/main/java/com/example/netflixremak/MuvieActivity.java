package com.example.netflixremak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.netflixremak.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MuvieActivity extends AppCompatActivity {
    private  TextView txtTitulo;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;

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
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
        //Metodo para trocar imagem dinamicamente
      LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this,R.drawable.shadows);
        if (drawable != null){
          Drawable movierCover = ContextCompat.getDrawable(this,R.drawable.movie_4);
          drawable.setDrawableByLayerId(R.id.coverDlawable,movierCover);
            ((ImageView)findViewById(R.id.image_couver_View)).setImageDrawable(drawable);
            getSupportActionBar().setTitle(null); // Para tirar o titulo da toobar
        }
        txtTitulo.setText("Batman Begins");
        txtDesc.setText("O jovem Bruce Wayne viaja para o Extremo Oriente, onde recebe treinamento em artes marciais do mestre Henri Ducard, um membro da misteriosa Liga das Sombras. Quando Ducard revela que a verdadeira proposta da Liga é a destruição completa da cidade de Gotham, Wayne retorna à sua cidade com o intuito de livrá-la de criminosos e assassinos. Com a ajuda do mordomo Alfred e do expert Lucius Fox, nasce Batman.");
        txtCast.setText(getString(R.string.Cast,"Earle" +",Rutger Hauer" +",Carmine Falcone" +",Tom Wilkinson"
                        +",Finch" +",Larry Holden" +",Martha Wayne" +",Sara Stewart"
                        +",Joe Chill" +",Richard Brake" + ",Homeless Man" +",Rade Serbedzija"  +",Faden's Limo Driver"));
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
       Movie movie = new Movie();
       movies.add(movie);
        }
        recyclerView.setAdapter(new MovieAdapter(movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
    }
    private static class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView ImageUrl;

        public MovieHolder (@NonNull View itemView) {
            super(itemView); // -> itemView que vem do conteiner principal muve_item pra contruir
            // quem constroi é o Adapter
            ImageUrl = itemView.findViewById(R.id.image_view_cover2);
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
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
        }
            // holder.ImageUrl.setImageResource(movie.getCoverUrl());

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}