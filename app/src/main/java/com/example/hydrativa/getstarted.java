package com.example.hydrativa;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class getstarted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_getstarted);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.startButton);
        ImageView photo = findViewById(R.id.photo);
        ImageView title = findViewById(R.id.title);

        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up); // Button scale-up animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in); // Photo fade-in animation

        ObjectAnimator titleMoveDown = ObjectAnimator.ofFloat(title, "translationY", 100f); // Adjust value for desired distance
        titleMoveDown.setDuration(1000);

        button.setAlpha(0f);
        button.setVisibility(View.VISIBLE);

        photo.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> {
            button.animate()
                    .alpha(1f)
                    .setDuration(3000)
                    .start();

            photo.setVisibility(View.VISIBLE);
            photo.startAnimation(fadeIn);
            titleMoveDown.start();

        }, 3000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getstarted.this, Login.class);
                startActivity(i);
            }
        });
    }
}