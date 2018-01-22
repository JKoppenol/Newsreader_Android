package com.example.jeroe.inhollandnewsreader_student550395.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jeroe.inhollandnewsreader_student550395.Article;
import com.example.jeroe.inhollandnewsreader_student550395.ArticleListAdapter;
import com.example.jeroe.inhollandnewsreader_student550395.R;
import com.example.jeroe.inhollandnewsreader_student550395.User;

import java.util.ArrayList;

import static com.example.jeroe.inhollandnewsreader_student550395.Activities.DetailActivity.CONTENT;
import static com.example.jeroe.inhollandnewsreader_student550395.R.id.drawer;
import static com.example.jeroe.inhollandnewsreader_student550395.R.id.main_login_button;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, View.OnClickListener {
    public static final String USER = "com.example.jeroe.inhollandnewsreader_student550395.Activities.MainActivity.CONTENT";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mVerticalList;
    private ProgressBar mProgress;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private RelativeLayout mDrawerPanel;
    private Boolean mDrawerOpened;
    private Button mLoginButton;

    private User mUser;
    private String mUsername;

    private TextView mGreeting;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ArticleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences("LoggedInUser", 0);
        editor = pref.edit();

        mDrawerOpened = false;

        mLoginButton = findViewById(main_login_button);
        mLoginButton.setOnClickListener(this);

        mProgress = findViewById(R.id.progressbar);

        mUsername = pref.getString("user_name", null);
        mGreeting = findViewById(R.id.greeting);

        if(mUsername != null){
            mGreeting.setText(getString(R.string.greeting, mUsername));
            mLoginButton.setText("Uitloggen");
        }

        else
            mGreeting.setText(getString(R.string.greeting, getString(R.string.default_user)));

        setupRecyclerView();
        initDrawer();
        fetchContent();
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.article_list);
        mVerticalList = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mVerticalList);

        adapter = new ArticleListAdapter(this, new ArrayList<Article>(), mProgress);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = adapter.getItemCount();
                int lastVisibleItem = mVerticalList.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItem + 10)) {
                    adapter.onLoadMore();
                }
            }
        });
    }

    private void fetchContent() {
        mRecyclerView.animate().alpha(0);
        mProgress.setVisibility(View.VISIBLE);
        mProgress.animate().alpha(1);
        adapter.reload();
    }

    private void initDrawer() {
        mDrawer = findViewById(drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        if (mUser == null) {
            mLoginButton = findViewById(R.id.main_login_button);
            mLoginButton.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                fetchContent();
                return true;
            case R.id.hamburger:
                toggleMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleMenu() {
        mDrawerPanel = findViewById(R.id.drawerPanel);

        if(mDrawerOpened){
            mDrawer.closeDrawer(mDrawerPanel);
            mDrawerOpened = false;
        }
        else{
            mDrawer.openDrawer(mDrawerPanel);
            mDrawerOpened = true;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Article article = adapter.getItem(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(CONTENT, article);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == main_login_button) {
            Button loginButton = findViewById(view.getId());

            if(loginButton.getText() == "Uitloggen") {
                editor.clear();
                mUser = null;
                mUsername = null;
                mGreeting.setText(getString(R.string.greeting, getString(R.string.default_user)));
                loginButton.setText("Login");
                editor.commit();
            }
            else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}