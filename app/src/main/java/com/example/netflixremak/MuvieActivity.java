package com.example.netflixremak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class MuvieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muvie);

        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }
        //Metodo para trocar imagem dinamicamente
      LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this,R.drawable.shadows);
        if (drawable != null){
          Drawable movierCover = ContextCompat.getDrawable(this,R.drawable.movie_4
          );
          drawable.setDrawableByLayerId(R.id.coverDlawable,movierCover);
            ((ImageView)findViewById(R.id.image_couver_View)).setImageDrawable(drawable);
        }

    }
}