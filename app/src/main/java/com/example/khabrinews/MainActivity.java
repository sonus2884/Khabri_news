package com.example.khabrinews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView contentRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentRecyclerViewAdapter mContentAdapter;

    private RecyclerView playlistRecyclerView;
    private PlaylistRecyclerViewAdapter mPlaylistAdapter;

    private List<MediaObject> contentList;
    private List<MediaObject> playList;

    //Exo Player..
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;

    private LinearLayout playlist_layout, content_list_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlist_layout = findViewById(R.id.playlist_layout);
        content_list_layout = findViewById(R.id.content_list_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contentList = new ArrayList<>(Arrays.asList(MediaDetails.MEDIA_OBJECTS));
        playList = new ArrayList<>();
        LayoutVisibility();

        //RecyclerView Content List..
        contentRecyclerView = findViewById(R.id.cl_recyclerView);
        addContentMethod();

        //PlayList RecyclerView..
        playlistRecyclerView = findViewById(R.id.pl_recyclerView);
        removePlaylistMethod();

        //Exo PLayer..
        exoPlayerView =findViewById(R.id.exo);
        PlaySong();

    }

    public void LayoutVisibility(){
        if(contentList.size() ==0 && playList.size() ==0) {
            content_list_layout.setVisibility(View.GONE);
            playlist_layout.setVisibility(View.GONE);
        }
        else if(playList.size() !=0 && contentList.size()==0){
            content_list_layout.setVisibility(View.GONE);
            playlist_layout.setVisibility(View.VISIBLE);
        }
        else if(playList.size()==0 && contentList.size() !=0){
            content_list_layout.setVisibility(View.VISIBLE);
            playlist_layout.setVisibility(View.GONE);
        }
        else {
            content_list_layout.setVisibility(View.VISIBLE);
            playlist_layout.setVisibility(View.VISIBLE);
        }
    }


    private void addContentMethod() {

        contentRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        contentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        contentRecyclerView.setLayoutManager(mLayoutManager);
        mContentAdapter = new ContentRecyclerViewAdapter(MainActivity.this,contentList);
        contentRecyclerView.setAdapter(mContentAdapter);
        mContentAdapter.setOnItemClickListener(new ContentRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                addItems(position);

            }
        });
        

    }
    private void removePlaylistMethod() {
        playlistRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        playlistRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playlistRecyclerView.setLayoutManager(mLayoutManager);

        mPlaylistAdapter = new PlaylistRecyclerViewAdapter(MainActivity.this,playList);
        playlistRecyclerView.setAdapter(mPlaylistAdapter);

        mPlaylistAdapter.setOnItemClickListener(new PlaylistRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                RemoveItem(position);
            }
        });
    }

    public void RemoveItem(int position){

//        PlaySong(position);
        contentList.add(playList.get(position));
        playList.remove(position);
        LayoutVisibility();
        mContentAdapter.notifyDataSetChanged();
        mPlaylistAdapter.notifyDataSetChanged();
    }

    public void addItems(int position){

        playList.add(contentList.get(position));
        contentList.remove(position);
        LayoutVisibility();
        mPlaylistAdapter.notifyDataSetChanged();
        mContentAdapter.notifyDataSetChanged();
    }

    public void  PlaySong(){

        try
        {
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
////            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector);
//            Uri audiouri = Uri.parse(mObj.getMedia_url());
//            DefaultHttpDataSourceFactory dataSourceFactory  = new DefaultHttpDataSourceFactory("exoplayer_video");
//
//            MediaSource mediaSource = new ExtractorMediaSource(audiouri,dataSourceFactory,extractorsFactory,null,null);
//            exoPlayerView.setPlayer(exoPlayer);
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.setPlayWhenReady(true);
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, getPackageName()), (TransferListener<? super DataSource>) bandwidthMeter);
            MediaSource[] mediaSources = new MediaSource[contentList.size()];
            for (int i = 0; i < mediaSources.length; i++) {
                String songUri = contentList.get(i).getMedia_url();
                mediaSources[i] = new ExtractorMediaSource(Uri.parse(songUri), dateSourceFactory,
                        extractorsFactory, null, null);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0]
                    : new ConcatenatingMediaSource(mediaSources);
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);



        }
        catch (Exception e)
        {
            Log.e("MainActivity","Exoplayer Error"+e.toString());
        }
    }
}
