package com.example.khabrinews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MediaObject > removePlaylist;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    PlaylistRecyclerViewAdapter() {
    }

    PlaylistRecyclerViewAdapter(Context context, List<MediaObject > removePlaylist) {

        this.mContext = context;
        this.removePlaylist = removePlaylist;
    }

    @NonNull
    @Override
    public PlaylistRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item_list, parent, false);
        MyViewHolder view = new MyViewHolder(v,mListener);
        return  view;

    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistRecyclerViewAdapter.MyViewHolder holder, int position) {

        MediaObject mediaObject = removePlaylist.get(position);
        holder.playlist_heading.setText(mediaObject.getTitle());
        holder.playlist_description.setText(mediaObject.getDescription());
        Picasso
                .with(mContext)
                .load(mediaObject.getImage_uri())
                .placeholder(R.drawable.news)
                .into(holder.playlist_image);

    }

    @Override
    public int getItemCount() {
        return removePlaylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout playlist_layout;
        ImageView removeButton;
        TextView playlist_heading;
        TextView playlist_description;
        ImageView playlist_image;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            playlist_layout = itemView.findViewById(R.id.playlist_layout);
            removeButton = itemView.findViewById(R.id.playlist_remove);
            playlist_heading = itemView.findViewById(R.id.playlist_heading_text);
            playlist_description = itemView.findViewById(R.id.playlist_description_text);
            playlist_image = itemView.findViewById(R.id.playlist_news_image);


            removeButton.setOnClickListener(new View.OnClickListener() {
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
