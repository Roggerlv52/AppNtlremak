package com.example.netflixremak.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.netflixremak.model.Movie;
import com.example.netflixremak.model.MovieDtalil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDtalil> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDtalilLoader movieDtalilLoader;

    @SuppressWarnings("deprecation")
    public MovieDetailTask(Context context) {
        this.context =new WeakReference<>(context);
    }

    public void setMovieDtalilLoader(MovieDtalilLoader movieDtalilLoader) {
        this.movieDtalilLoader = movieDtalilLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if (context != null)
            dialog = ProgressDialog.show(context,"Carregando","",true);
    }

    @Override
    protected MovieDtalil doInBackground(String... params) {
        String url = params[0];
        try {
            URL requestUrl = new URL(url);

            HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCodigo = urlConnection.getResponseCode();
            if (responseCodigo > 400) {
                throw new IOException("Error");
            }
            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            String jsonAsString = toString(in);

            MovieDtalil movieDetail = getMovieDetail(new JSONObject(jsonAsString));
            in.close();
            return movieDetail;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MovieDtalil getMovieDetail(JSONObject json) throws JSONException{
        int id = json.getInt("id");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");

        List<Movie>movies = new ArrayList<>();
        JSONArray movieArray = json.getJSONArray("movie");
        for (int i = 0; i <movieArray.length() ; i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            String c = movie.getString("cover_url");
            int idSimilar = movie.getInt("id");
            Movie similar = new Movie();
            similar.setId(idSimilar);
            similar.setCoverUrl(c);

            movies.add(similar);

        }
        Movie movie = new Movie();
        movie.setId(id);
        movie.setCoverUrl(coverUrl);
        movie.setTitle(title);
        movie.setDesc(desc);
        movie.setCast(cast);
        return  new MovieDtalil(movie,movies);
    }

    @Override
    protected void onPostExecute(MovieDtalil movieDtalil) {
        super.onPostExecute(movieDtalil);
        dialog.dismiss();
        if (movieDtalilLoader != null)
            movieDtalilLoader.onResult(movieDtalil);

    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return baos.toString();
    }

    public interface MovieDtalilLoader{
        void onResult(MovieDtalil movieDtalil);
    }
}
