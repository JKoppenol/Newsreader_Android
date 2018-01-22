package com.example.jeroe.inhollandnewsreader_student550395.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jeroe.inhollandnewsreader_student550395.Article;
import com.example.jeroe.inhollandnewsreader_student550395.R;
import com.example.jeroe.inhollandnewsreader_student550395.Services.ArticleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CONTENT = "com.example.jeroe.inhollandnewsreader_student550395.Activities.DetailActivity.CONTENT";

    private Article mContent;
    private String mAuthtoken;
    private ArticleService mArticleService;
    private Button favoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mArticleService = retrofit.create(ArticleService.class);

        setContentView(R.layout.activity_detail);
        mContent = getIntent().getParcelableExtra(CONTENT);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("LoggedInUser", 0);
        mAuthtoken = pref.getString("authtoken", null);
        setupUI();
    }

    private void setupUI() {
        favoriteBtn = findViewById(R.id.favorite_button);
        ImageView imageView = findViewById(R.id.detail_image);
        Glide.with(this).load(mContent.Image).centerCrop().crossFade().into(imageView);
        ((TextView) findViewById(R.id.detail_title)).setText(mContent.Title);
        ((TextView) findViewById(R.id.detail_description)).setText(mContent.Summary);

        if(mAuthtoken != null) {
            favoriteBtn.setVisibility(View.VISIBLE);

            if(mContent.IsLiked) {
                favoriteBtn.setText(getResources().getString(R.string.favorite_on));
                favoriteBtn.setTextColor(Color.RED);
            }

            else {
                favoriteBtn.setText(getResources().getString(R.string.favorite_off));
                favoriteBtn.setTextColor(Color.GRAY);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_detail_share) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, mContent.Title);
            sendIntent.putExtra(Intent.EXTRA_TEXT, mContent.Summary);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        likeArticle();
    }

    private void likeArticle() {
        if (!mContent.IsLiked) {
            mArticleService.putLikeArticle(mAuthtoken, mContent.Id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    mContent.IsLiked = true;
                    favoriteBtn.setText(getResources().getString(R.string.favorite_on));
                    favoriteBtn.setTextColor(Color.RED);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    favoriteBtn.setText(getResources().getString(R.string.favorite_off));
                    favoriteBtn.setTextColor(Color.GRAY);
                }
            });
        } else {
            mArticleService.deleteLikeArticle(mAuthtoken, mContent.Id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    mContent.IsLiked = false;
                    favoriteBtn.setText(getResources().getString(R.string.favorite_off));
                    favoriteBtn.setTextColor(Color.GRAY);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    favoriteBtn.setText(getResources().getString(R.string.favorite_on));
                    favoriteBtn.setTextColor(Color.RED);
                }
            });
        }
    }
}
