package com.mathias.youtube.api;

import com.mathias.youtube.model.Resultado;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    @GET("search")
  Call<Resultado> recuperarVideos(
                    @Query("part") String part,
                    @Query("order") String order,
                    @Query("maxResults") String maxResults,
                    @Query("key") String key,
                    @Query("channelId") String channelId,
                    @Query("q") String q
                    );
}
