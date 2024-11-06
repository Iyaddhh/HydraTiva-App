package com.example.hydrativa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.example.hydrativa.adapters.ImageAdapter;
import com.example.hydrativa.models.ImageItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {

    ViewPager2 viewPage;
    private ViewPager2.OnPageChangeCallback pageChangeListener;
    private ImageView[] dotsImage;
    private Handler handler;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        androidx.cardview.widget.CardView gridKebun1 = findViewById(R.id.grid1);
        androidx.cardview.widget.CardView gridKebun2 = findViewById(R.id.grid2);
        androidx.cardview.widget.CardView gridKebun3 = findViewById(R.id.grid3);
        androidx.cardview.widget.CardView gridKebun4 = findViewById(R.id.grid4);

        gridKebun1.setOnClickListener(view -> {
            String url = "https://www.youtube.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        gridKebun2.setOnClickListener(view -> {
            String url = "https://www.youtube.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        gridKebun3.setOnClickListener(view -> {
            String url = "https://www.youtube.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        gridKebun4.setOnClickListener(view -> {
            String url = "https://www.youtube.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        BottomAppBar bottomAppBar = findViewById(R.id.bottomView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        FloatingActionButton fab = findViewById(R.id.fab);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_settings) {
                startActivity(new Intent(Dashboard.this, Setting.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        fab.setOnClickListener(view -> {
            startActivity(new Intent(Dashboard.this, Watering.class));
            overridePendingTransition(0, 0);
        });

        viewPage = findViewById(R.id.viewpager);
        LinearLayout slideDot = findViewById(R.id.slider);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);

        ArrayList<ImageItem> imageList = new ArrayList<>();
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.stevia));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.tehdia));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.daun_bg));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.stevia));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.tehdia));
        imageList.add(new ImageItem(UUID.randomUUID().toString(), R.drawable.daun_bg));

        ImageAdapter imageAdapter = new ImageAdapter();
        viewPage.setAdapter(imageAdapter);
        imageAdapter.submitList(imageList);

        dotsImage = new ImageView[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {
            dotsImage[i] = new ImageView(this);
            dotsImage[i].setImageResource(R.drawable.non_active_dot);
            slideDot.addView(dotsImage[i], params);
        }
        dotsImage[0].setImageResource(R.drawable.active_dot);

        pageChangeListener = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                for (int i = 0; i < dotsImage.length; i++) {
                    if (i == position) {
                        dotsImage[i].setImageResource(R.drawable.active_dot);
                    } else {
                        dotsImage[i].setImageResource(R.drawable.non_active_dot);
                    }
                }
            }
        };

        viewPage.registerOnPageChangeCallback(pageChangeListener);

        handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == imageList.size()) {
                    currentPage = 0;
                }
                viewPage.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewPage != null) {
            viewPage.unregisterOnPageChangeCallback(pageChangeListener);
        }
    }
}