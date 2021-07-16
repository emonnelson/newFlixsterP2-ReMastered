package com.example.newflixster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import models.Movies;
import okhttp3.Headers;

public class DetailsActivity extends YouTubeBaseActivity {
    public static final String YOUTUBE_API_KEY = "AIzaSyAWTnZ8xsBK_IIsPFq6N0kpp2ZISc1JV68";
    public static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    TextView tvTitle;
    TextView tv_overview;
    RatingBar ratingBar;
    YouTubePlayerView youTubeBaseActivity;
    ImageView posterIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvTitle = findViewById(R.id.tvTitle);
        tv_overview = findViewById(R.id.tv_overview);
        ratingBar = findViewById(R.id.ratingBar);
        youTubeBaseActivity = findViewById(R.id.player);
        posterIv = findViewById(R.id.posterIv);

       
        Movies movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tv_overview.setText(movie.getOverView());
        ratingBar.setRating((float)movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    if (movie.getRating() > 5)
                        initializeYoutube(youtubeKey, true);
                    else
                        initializeYoutube(youtubeKey, false);
                    Log.d("DetailsActivity", youtubeKey);
                    //initializeYoutube(youtubeKey);
                } catch (JSONException e) {
                    Log.e("DetailsActivity", "Failed to parse JSON", e);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });


    }

    private void initializeYoutube(String youtubeKey, boolean autoPlay) {
        youTubeBaseActivity.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DetailsActivity", "onInitializationSuccess");
                if(autoPlay)
                    youTubePlayer.loadVideo(youtubeKey,1);
                else
                youTubePlayer.cueVideo(youtubeKey);

                //When trying to exit from the activity on full screen
                youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                    @Override
                    public void onFullscreen(boolean b) {
                        if(!b){
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DetailsActivity", "onInitializationFailure");

            }
        });

        String imgurl1 = getIntent().getStringExtra("poster_path");
        //Movies m = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        Picasso.get().load(imgurl1).into(posterIv);
    }
