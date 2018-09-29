package com.example.root.appcontest.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.appcontest.R;
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

        //받아온 데이터들을 가지고 활용해서 페이지를 만든다.
        title.setText("바닷가 공연");
        image.setImageResource(R.drawable.background_image_main);
        period.setText("2018/12/23 ~ 2016/08/24");
        content.setText("세월 지나도\n난 변하지 않아.\nand then I cry for you.\n이 밤 지나면\n이젠 안녕\n영원히~~~~~~~~~~\nnaver.com\n010-2070-6774");

        // 태그 세팅
        String tags = "태그,잘되나?,테스트테스트,아아마이크,테스토스테론";
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
}
