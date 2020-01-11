package com.example.khabrinews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContentRecyclerViewAdapter extends RecyclerView.Adapter<ContentRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MediaObject> addContentList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ContentRecyclerViewAdapter(){

    }

    ContentRecyclerViewAdapter(Context context,List<MediaObject> addContent){

        this.mContext = context;
        this.addContentList = addContent;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_list, parent, false);
        MyViewHolder view = new MyViewHolder(v,mListener);
        return  view;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final MediaObject mediaObject = addContentList.get(position);

        holder.content_list_heading.setText(mediaObject.getTitle());
        holder.content_list_discription.setText(mediaObject.getDescription());

        Picasso
                .with(mContext)
                .load(mediaObject.getImage_uri())
                .placeholder(R.drawable.news)
                .into(holder.content_list_image);

    }

    @Override
    public int getItemCount() {

        return addContentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout contentlist_layout;
        ImageView addButton;
        TextView content_list_heading;
        TextView content_list_discription;
        ImageView content_list_image;

        public MyViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);

            contentlist_layout = itemView.findViewById(R.id.content_list_layout);
            addButton = itemView.findViewById(R.id.content_add);
            content_list_heading = itemView.findViewById(R.id.content_list_heading_text);
            content_list_discription = itemView.findViewById(R.id.content_list_description_text);
            content_list_image = itemView.findViewById(R.id.content_list_news_image);


            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
