package com.example.movies.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movies.Adapters.FilmListAdapter;
import com.example.movies.Adapters.SliderAdapters;
import com.example.movies.Domain.ListFilm;
import com.example.movies.Domain.SliderItems;
import com.example.movies.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestMovies, AdapterUpComing, AdapterCategory;
    private RecyclerView recyclerViewBestMovies, recyclerviewUpcoming, recyclerViewCategory;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest, mStringRequest2, mStringRequest3;
    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        banners();
        sendRequestUpcoming();
        sendRequestBestMovies();

    }

    private void sendRequestBestMovies() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response,ListFilm.class);
            adapterBestMovies = new FilmListAdapter(items);
            recyclerViewBestMovies.setAdapter(adapterBestMovies);
        }, error -> {
            loading1.setVisibility(View.GONE);
            Log.i("Error Report", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);

    }

    private void sendRequestUpcoming() {
        mRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        mStringRequest3 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=2", response -> {
            Gson gson = new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response,ListFilm.class);
            AdapterUpComing = new FilmListAdapter(items);
            recyclerviewUpcoming.setAdapter(AdapterUpComing);
        }, error -> {
            loading3.setVisibility(View.GONE);
            Log.i("Error Report", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest3);

    }

    private void banners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        viewPager2.setAdapter(new SliderAdapters(sliderItems, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer(){
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
            }
        });

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
          viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewpagerSlider);
        recyclerViewBestMovies = findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        recyclerviewUpcoming = findViewById(R.id.view3);
        recyclerviewUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory = findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBar3);
    }


}