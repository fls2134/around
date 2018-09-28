package com.example.root.appcontest.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.appcontest.R;
import com.google.android.flexbox.FlexboxLayout;


/**
 * Show information of Item
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView title = findViewById(R.id.text_title);
        ImageView image = findViewById(R.id.image);
        TextView period = findViewById(R.id.text_period);
        TextView content = findViewById(R.id.text_content);
        FlexboxLayout flexboxLayout = findViewById(R.id.info_flexbox);

        //받아온 데이터들을 가지고 활용해서 페이지를 만든다.
        title.setText("제목제목제목제목제목제목");
        image.setImageResource(R.drawable.background_image_main);
        period.setText("2018/12/23 ~ 2016/08/24");
        content.setText("내내용내용내용내용내용내용내용내용용내용내용내용\n 내용내용내용내용 http://naver.com \n 내용내용내용내용sdfodf \n원하나 자세한내용?? 링크 ㄱㄱ naver.com \n google.com \n sdoafiwjfiwjeoi 나에게 전화를 걸어 010-9395-5376 \n 0514425554");

        String tags = "태그,잘되나?,테스트테스트,아아마이크,테스토스테론";
        String words[] = tags.split(",");
        for (int i = 0; i < words.length; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextColor(Color.parseColor("#26C6DA"));
            textView.setText('#'+words[i]);
            textView.setTextSize(20);
            flexboxLayout.addView(textView);
        }

    }
}
