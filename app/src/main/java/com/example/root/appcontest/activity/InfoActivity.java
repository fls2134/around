package com.example.root.appcontest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.root.appcontest.R;
import com.example.root.appcontest.model.LocalData;
import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;


/**
 * Show information of Item
 */
public class InfoActivity extends AppCompatActivity {
    /**
     * 뷰 관련 변수
     */
    TextView title;
    ImageView image;
    TextView period;
    TextView content;
    FlexboxLayout flexboxLayout;
    Button button;

    LocalData data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        title = findViewById(R.id.text_title);
        image = findViewById(R.id.image);
        period = findViewById(R.id.text_period);
        content = findViewById(R.id.text_content);
        flexboxLayout = findViewById(R.id.info_flexbox);
        button = findViewById(R.id.closebtn_info);

        getIntentData();
        //받아온 데이터들을 가지고 활용해서 페이지를 만든다.
        setPage();

    }
    private void getIntentData()
    {
        data = (LocalData)getIntent().getSerializableExtra("data");
    }
    private void setPage()
    {
        title.setText(data.title);
        image.setImageResource(R.drawable.background_image_main);
        String p = data.sYear + "/" + data.sMonth + "/" +data.sDay + " ~ " + data.eYear + "/" + data.eMonth + "/" +data.eDay;
        period.setText(p);
        content.setText(data.content);

        //이미지 세팅
        final ProgressBar progressBar = findViewById(R.id.info_progressbar);
 //       final ImageView imageView = (ImageView) findViewById(R.id.img_glide);
        Glide.with(this).load(data.img_url).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(image);

        // 태그 세팅
        String tags = data.tag;
        String words[] = tags.split(",");
        // 태그 마진
        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l.setMargins(5,5,5,5);
        //태그 생성
        for (String word : words) {
            TextView textView = new TextView(getApplicationContext());
            textView.setBackgroundColor(getResources().getColor(R.color.white));
            textView.setTextColor(getResources().getColor(R.color.tagbox));
            textView.setText('#' + word);
            textView.setTextSize(20);
            textView.setLayoutParams(l);
            textView.setPadding(5, 5, 5, 5);
            flexboxLayout.addView(textView);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data = (LocalData)intent.getSerializableExtra("data");
    }
}
