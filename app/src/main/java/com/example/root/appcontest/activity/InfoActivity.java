package com.example.root.appcontest.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.root.appcontest.R;
import com.example.root.appcontest.model.LocalData;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


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
    ImageView button;
    ImageView favoriteButton;
    LocalData data;
    LocalData tmp;

    //DB 관련 변수
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    StorageReference testRef;

    /**
     * 좋아요 모드 확인을 위한 모드 변수
     * 0 = editmode
     * 1 = favorite unchecked
     * 2 = favorite checked
     */
    private int modeFavorite = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_detail);

        // 뷰 세팅
        title = findViewById(R.id.info_detail_title);
        image = findViewById(R.id.info_detail_poster);
        period = findViewById(R.id.info_detail_date);
        content = findViewById(R.id.info_detail_content);
        flexboxLayout = findViewById(R.id.info_detail_flexbox);
        button = findViewById(R.id.info_detail_back_btn);
        favoriteButton = findViewById(R.id.info_detail_util_btn);

        // 데이터 정보 처리
        getIntentData();
        setFavoriteMode();
        // 좋아요 정보 초기화
        if (modeFavorite == 0) {
            favoriteButton.setImageResource(R.drawable.ic_delete);
        } else if (modeFavorite == 1) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
        } else if (modeFavorite == 2) {
            favoriteButton.setImageResource(R.drawable.ic_favorite);
        }

        //받아온 데이터를 사용해 페이지를 생성
        setPage();

    }

    private void getIntentData() {
        data = (LocalData) getIntent().getSerializableExtra("data");
    }

    private void setFavoriteMode() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        String nickname;
        nickname = pref.getString("nickname_text", "닉네임");


        Set<String> stringSet;
        SharedPreferences prefFav = getSharedPreferences("favorites", MODE_PRIVATE);
        stringSet = prefFav.getStringSet("favorite", null);
        if (stringSet == null) {
            String[] array = {};
            stringSet = new HashSet<String>(Arrays.asList(array));
        }

        //String[] strings = stringSet.toArray();


        if (nickname.compareTo(data.nickname) == 0) {
            // 작성자일 경우 modeFavorite = 0
            modeFavorite = 0;
        } else if (stringSet.contains(data.id + "")) {
            // 작성자는 아닌데 로컬에 매칭되는 항목이 존재 modeFavorite = 2
            modeFavorite = 2;
        } else {
            // 작성자는 아닌데 로컬에 매칭되는 항목이 없을 경우 modeFavorite = 1
            modeFavorite = 1;
        }
    }

    private void setPage() {
        // 페이지 생성
        title.setText(data.title);
        image.setImageResource(R.drawable.background_image_main);
        String p = data.sYear + "/" + data.sMonth + "/" + data.sDay + " ~ " + data.eYear + "/" + data.eMonth + "/" + data.eDay;
        period.setText(p);
        content.setText(data.content);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("Local_info");

        //이미지 세팅
        final ProgressBar progressBar = findViewById(R.id.info_detail_progressbar);
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
        l.setMargins(5, 5, 5, 5);
        //태그 생성
        for (String word : words) {
            TextView textView = new TextView(getApplicationContext());
            textView.setBackgroundColor(Color.argb(55,0,0,0));
            textView.setTextColor(getResources().getColor(R.color.white));
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

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> stringSet;
                SharedPreferences pref = getSharedPreferences("favorites", MODE_PRIVATE);
                stringSet = new HashSet<String>(pref.getStringSet("favorite", new HashSet<String>()));
                if (stringSet == null) {
                    String[] array = {};
                    stringSet = new HashSet<String>(Arrays.asList(array));
                }

                String id_str = data.id + "";
                if (modeFavorite == 0) {
                    //data.id => 얘를 찾아가서 DB에서 지워주세요
                    databaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                                if (data.id == messageData.getValue(LocalData.class).id) {
                                    FirebaseStorage.getInstance().getReferenceFromUrl(messageData.getValue(LocalData.class).img_url).delete();
                                    messageData.getRef().setValue(null);
                                }
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_LONG);
                        }
                    });
                } else if (modeFavorite == 1) {
                    //좋아요 클릭
                    modeFavorite = 2;
                    favoriteButton.setImageResource(R.drawable.ic_favorite);
                    stringSet.add(id_str);
                } else if (modeFavorite == 2) {
                    //좋아요 취소
                    modeFavorite = 1;
                    favoriteButton.setImageResource(R.drawable.ic_favorite_empty);
                    stringSet.remove(id_str);
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putStringSet("favorite", stringSet);
                String[] strary = stringSet.toArray(new String[stringSet.size()]);
                for (int i = 0; i < stringSet.size(); i++) {
                }
                editor.apply();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data = (LocalData) intent.getSerializableExtra("data");
    }
}
