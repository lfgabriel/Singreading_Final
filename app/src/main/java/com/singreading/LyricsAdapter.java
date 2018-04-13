package com.singreading;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.singreading.model.Lyric;

import java.util.List;

/**
 * Created by Gabriel Franco on 13/04/18.
 */

public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.LyricsAdapterViewHolder> {

    private List<Lyric> mLyricData;
    private Context context;


    private final LyricsAdapterOnClickHandler mClickHandler;


    public interface LyricsAdapterOnClickHandler {
        void onClick(Lyric lyricDetails);
    }

    public LyricsAdapter(Context context, LyricsAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }


    public class LyricsAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mLyricsTextView;

        public LyricsAdapterViewHolder(View view) {
            super(view);
            mLyricsTextView = (TextView) view.findViewById(R.id.tv_lyric_artist_name);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Lyric lyricDetails = mLyricData.get(adapterPosition);
            mClickHandler.onClick(lyricDetails);
        }
    }

    @Override
    public LyricsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.lyric_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new LyricsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LyricsAdapterViewHolder lyricsAdapterViewHolder, int position) {;
        lyricsAdapterViewHolder.mLyricsTextView.setText(mLyricData.get(position).getArtist()
        + "\n" + mLyricData.get(position).getName());
    }

    @Override
    public int getItemCount() {

        if (mLyricData  == null) return 0;
        return mLyricData.size();

    }

    public void setLyricsData(List<Lyric> lyricsData) {
        mLyricData = lyricsData;
        notifyDataSetChanged();
    }


}
