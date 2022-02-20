package com.example.netflixremak.kotilin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflixremak.MuvieActivity
import com.example.netflixremak.R
import com.example.netflixremak.model.Categori
import com.example.netflixremak.model.Movie
import com.example.netflixremak.util.CategoryTask
import com.example.netflixremak.util.ImagemDowloaderTesk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category.view.*
import kotlinx.android.synthetic.main.muve_item.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var mainAdapiter: MainAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categoris = arrayListOf<Categori>()
        mainAdapiter = MainAdapter(categoris)
        Recyclerview_main.adapter = mainAdapiter
        Recyclerview_main.layoutManager = LinearLayoutManager(this)

        val categoryTask = CategoryTask(this)
        categoryTask.setCategoryLoader {

           mainAdapiter.categories.clear()
            mainAdapiter.categories.addAll(it)
            mainAdapiter.notifyDataSetChanged()
        }

        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home")
    }

    private inner class MainAdapter(val categories: MutableList<Categori>) :
        RecyclerView.Adapter<CategoyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoyHolder {
            return CategoyHolder(layoutInflater.inflate(R.layout.category, parent, false))
        }
        override fun getItemCount(): Int = categories.size
        override fun onBindViewHolder(holder: CategoyHolder, position: Int) {
            val category = categories[position]
            holder.bind(category)
        }
    }

    private inner class Moviedapter(val movies: List<Movie>) : RecyclerView.Adapter<MovieHolder>() {
        //para pegar a posição
        val onClick:((Int)-> Unit)? = { position->
         if(movies[position].id <= 3 ){
            val intent = Intent(this@MainActivity,MuvieActivity::class.java)
             intent.putExtra("id",movies[position].id)
             startActivity(intent)
         }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            return MovieHolder(layoutInflater.inflate(R.layout.muve_item,
                parent, false),
            onClick
            )
        }
        override fun getItemCount(): Int = movies.size
        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]
            holder.bind(movie)
        }
    }

    private inner class CategoyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Categori) {
            itemView.text_view_titulo.text = category.name
            itemView.recycleview_Move.adapter = Moviedapter(category.movies)
            itemView.recycleview_Move.layoutManager = LinearLayoutManager(
                this@MainActivity,RecyclerView.HORIZONTAL,false)
        }

    }
//para evento de click nas imagem
    private inner class MovieHolder(itemView: View,val onClick:( (Int )-> Unit)?) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            ImagemDowloaderTesk(itemView.image_view_cover1).execute(
                movie.coverUrl
            )
            itemView.image_view_cover1.setOnClickListener{
              onClick?.invoke(adapterPosition)
            }
        }
    }
}