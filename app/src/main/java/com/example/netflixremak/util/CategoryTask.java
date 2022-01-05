package com.example.netflixremak.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.netflixremak.model.Categori;
import com.example.netflixremak.model.Movie;

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


public class CategoryTask extends AsyncTask<String, Void, List<Categori>> {
    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;


    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }
    public void setCategoryLoader(CategoryLoader categoryLoader){
        this.categoryLoader = categoryLoader;
    }
    //main-threadd
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if (context != null)
        dialog = ProgressDialog.show(context, "carregando", "", true);
    }

    //thread-background
    @Override
    protected List<Categori> doInBackground(String... params) {
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

            List<Categori> categoris = getCategoris(new JSONObject(jsonAsString));
            in.close();
            return categoris;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Para converter json para java
    private List<Categori> getCategoris(JSONObject json) throws JSONException {

        List<Categori> categoris = new ArrayList<>();
        JSONArray categoryArray = json.getJSONArray("category");
        for (int i = 0; i < categoryArray.length(); i++) {

            JSONObject category = categoryArray.getJSONObject(i);
            String title = category.getString("title");
            List<Movie> movies = new ArrayList<>();
            JSONArray movieArray = category.getJSONArray("movie");
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movie = movieArray.getJSONObject(j);
                String coverUrl = movie.getString("cover_url");
                Movie movieObj = new Movie();
                movieObj.setCoverUrl(coverUrl);
                movies.add(movieObj);

            }
            Categori categoriObj = new Categori();
            categoriObj.setName(title);
            categoriObj.setMovies(movies);
            categoris.add(categoriObj);
        }
        return categoris;
    }

    //main-threadd
    @Override
    protected void onPostExecute(List<Categori> categoris) {
        super.onPostExecute(categoris);
        dialog.dismiss();
        //listener
        if (categoryLoader != null)
            categoryLoader.onResult(categoris);
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
    public interface CategoryLoader{
        void onResult(List<Categori>categoris);

    }
}
