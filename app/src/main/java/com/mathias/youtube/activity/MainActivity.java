package com.mathias.youtube.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mathias.youtube.R;
import com.mathias.youtube.adapter.AdapterVideo;
import com.mathias.youtube.api.YoutubeService;
import com.mathias.youtube.helper.RetrofitConfig;
import com.mathias.youtube.helper.YoutubeConfig;
import com.mathias.youtube.listener.RecyclerItemClickListener;
import com.mathias.youtube.model.Item;
import com.mathias.youtube.model.Resultado;
import com.mathias.youtube.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    //widgets
    private RecyclerView recyclerVideos;


    private List<Item> videos = new ArrayList<>();
    private Resultado resultado;
    private AdapterVideo adapterVideo;
    private MaterialSearchView searchView;

    //retrofit
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializar componenetes
        recyclerVideos = findViewById(R.id.recyclerVideos);

        searchView = findViewById(R.id.searchView);

        //Configuracoes iniciais
        retrofit = RetrofitConfig.getRetrofit();


        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("YouTube");
        setSupportActionBar(toolbar);


          // Configura recyclerview



        //Recupera Videos
        recuperarVideos("");


        //Configura metodos para o Search View
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
               recuperarVideos("");
            }
        });






    }






   private void recuperarVideos(String pesquisa) {
        String q = pesquisa.replaceAll(" ","+");
        YoutubeService youtubeService = retrofit.create(YoutubeService.class);

        youtubeService.recuperarVideos("snippet",
                "date",
                "20",
                YoutubeConfig.CHAVE_YOUTUBE_API,
                YoutubeConfig.CANAL_ID,q
        ).enqueue(new Callback<Resultado>() {
                                            @Override
                                            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                                                Log.d("resultado", "resultado: " + response.toString());
                                                 if(response.isSuccessful()) {
                                                     resultado = response.body();
                                                     videos = resultado.items;
                                                     configurarRecyclerView();

                                                 }

                                            }

                                            @Override
                                            public void onFailure(Call<Resultado> call, Throwable t) {

                                            }


                                        }
        );
    }

    public void configurarRecyclerView() {
        adapterVideo = new AdapterVideo(videos,this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideo);

        //Configura evento de clique
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(
                this,
                recyclerVideos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Item video = videos.get(position);
                        String idVideo = video.id.videoId;

                        Intent i = new Intent(MainActivity.this,PlayerActivity.class);
                        i.putExtra("idVideo",idVideo);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;
    }
}