package com.example.root.appcontest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.root.appcontest.R;
import com.example.root.appcontest.model.TagButton;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class TagActivity extends AppCompatActivity implements View.OnClickListener{

    final int TAGS_CONCERT = 1;
    final int TAGS_RESTAURANT = 2;
    final int TAGS_EVENT = 3;

    ArrayList<String> tags = new ArrayList<>();
    FlexboxLayout flexboxLayout_tags;
    FlexboxLayout flexboxLayout_res;
    EditText tag_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        Intent intent = getIntent();
        int init_tags = intent.getIntExtra("init_tags", 0);
        tag_text = findViewById(R.id.tag_text);
        String[] strings;
        switch (init_tags)
        {
            case TAGS_CONCERT:
                strings = getResources().getStringArray(R.array.con_tags);
                break;
            case TAGS_RESTAURANT:
                strings = getResources().getStringArray(R.array.rest_tags);
                break;
            case TAGS_EVENT:
                strings = getResources().getStringArray(R.array.event_tags);
                break;
            default:
                strings = null;
        }

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("태그 설정");

        flexboxLayout_tags = findViewById(R.id.flexbox_layout_tags);
        flexboxLayout_res = findViewById(R.id.flexbox_layout_res);
        if(strings == null)
        {
            strings = new String[5];
            strings[0] = "카테고리를";
            strings[1] = "고르지 않아";
            strings[2] = "기본 태그가";
            strings[3] = "없습니다";
            strings[4] = "직접 추가해 주세요";
        }
        for (int i = 0; i < strings.length; i++) {
            TagButton button = new TagButton(getApplicationContext());
            button.setText(strings[i]);
            button.setOnClickListener(this);
            flexboxLayout_tags.addView(button);
        }

    }

    @Override
    public void onClick(View view) {
        String tag = ((Button)view).getText().toString();
        if(tags.contains(tag))
        {
            tags.remove(tag);
            flexboxLayout_res.removeView(view);
            flexboxLayout_tags.addView(view);
            view.setBackgroundResource(R.drawable.capsule);
            ((Button) view).setTextColor(Color.parseColor("#000000"));
        }
        else
        {
            tags.add(tag);
            flexboxLayout_tags.removeView(view);
            flexboxLayout_res.addView(view);
            view.setBackgroundResource(R.drawable.capsule_selected);
            ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
    public void Add_Tag(View v)
    {
        String tag = tag_text.getText().toString();
        tag_text.setText("");
        tags.add(tag);
        TagButton button = new TagButton(getApplicationContext());
        button.setText(tag);
        button.setOnClickListener(this);
        button.setBackgroundResource(R.drawable.capsule_selected);
        button.setTextColor(Color.parseColor("#FFFFFF"));
        flexboxLayout_res.addView(button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_complete:
                String tag_string = "";
                for (int i = 0; i < tags.size(); i++) {
                    if(i == tags.size()-1)
                        tag_string += tags.get(i);
                    else
                        tag_string = tag_string + tags.get(i) + ",";
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("tag", tag_string);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
