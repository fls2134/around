package com.example.root.appcontest.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.root.appcontest.R;
import com.example.root.appcontest.activity.InfoActivity;
import com.example.root.appcontest.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by sks on 2018. 9. 28..
 */
public class RCViewAdapter extends RecyclerView.Adapter<RCViewAdapter.MyViewHolder>{
    /**
     * RecyclerView 에 담겨질 아이템 리스트
     */
    ArrayList<CardItem> mList;
    Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImage;
        TextView nickName;

        CardView cardView;
        ImageView posterImage;
        TextView title;
        ProgressBar progressBar;

        int id;

        public MyViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.profile_image_card);
            nickName = view.findViewById(R.id.nickname_card);

            posterImage = view.findViewById(R.id.poster_card);
            title = view.findViewById(R.id.title_card);
            cardView = view.findViewById(R.id.cardview);
            progressBar = view.findViewById(R.id.progress);
        }

    }

    public RCViewAdapter(ArrayList<CardItem> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public RCViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewAdapter.MyViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.id = mList.get(position).getId();
        myViewHolder.profileImage.setImageResource(mList.get(position).profileImage);
        myViewHolder.nickName.setText(mList.get(position).nickName);
        final ProgressBar viewHolderProgressBar = myViewHolder.progressBar;
 //       final ImageView imageView = (ImageView) findViewById(R.id.img_glide);
        Glide.with(context).load(mList.get(position).imgurl).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        viewHolderProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        viewHolderProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(myViewHolder.posterImage);
        //myViewHolder.posterImage.setImageResource(mList.get(position).posterImage);
        myViewHolder.title.setText(mList.get(position).tilte);
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
