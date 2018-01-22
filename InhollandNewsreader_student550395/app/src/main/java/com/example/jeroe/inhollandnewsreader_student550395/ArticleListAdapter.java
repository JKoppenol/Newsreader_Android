package com.example.jeroe.inhollandnewsreader_student550395;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.jeroe.inhollandnewsreader_student550395.Activities.ListItemClickListener;
import com.example.jeroe.inhollandnewsreader_student550395.Services.ArticleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ListViewHolder> implements Callback<News> {

    private final List<Article> mItems;
    private final Context mContext;
    private final ListItemClickListener mItemClickListener;
    private final LayoutInflater layoutInflater;

    private SharedPreferences pref;

    private int nextId;
    private String mAuthtoken;
    private ArticleService mArticleService;
    private ProgressBar mProgress;

    public ArticleListAdapter(Context context, List<Article> items, ProgressBar progress) {
        mItems = items;
        mContext = context;
        mProgress = progress;

        layoutInflater = LayoutInflater.from(context);

        this.mItemClickListener = (ListItemClickListener) context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mArticleService = retrofit.create(ArticleService.class);

        pref = mContext.getSharedPreferences("LoggedInUser", 0);

        mAuthtoken = pref.getString("authtoken", null);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.listitem, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        Article node = getItem(position);

        holder.title.setText(node.Title);
        holder.description.setText(node.Summary);
        Glide.with(mContext).load(node.Image).centerCrop().crossFade().into(holder.image);

        final int clickedItem = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, clickedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Article getItem(int position) {
        return mItems.get(position);
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;

        ListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listitem_title);
            description = itemView.findViewById(R.id.listitem_description);
            image = itemView.findViewById(R.id.listitem_image);
        }
    }

    public void reload() {
        this.mItems.clear();
        nextId = -1;
        onLoadMore();
        refresh();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void onLoadMore() {
        if(mAuthtoken != null) {
            if (nextId < 0) {
                mArticleService.articles(mAuthtoken, null).enqueue(this);
            } else {
                mArticleService.article(mAuthtoken, nextId, 20).enqueue(this);
            }
        }

        else {
            if (nextId < 0) {
                mArticleService.articles(null).enqueue(this);
            } else {
                mArticleService.article(nextId, 20).enqueue(this);
            }
        }
    }

    @Override
    public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {
        if (response.isSuccessful() && response.body() != null) {
            News result = response.body();
            nextId = result.NextId;

            for (Article article : result.Results) {
                mItems.add(article);
            }

            notifyDataSetChanged();
        }

        hideSpinner();
    }

    @Override
    public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
        Log.e(this.getClass().toString().toUpperCase(), "Tried to connect, but something went wrong", t);
        hideSpinner();
    }

    private void hideSpinner() {
        mProgress.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                mProgress.setVisibility(View.GONE);
            }
        });
    }
}