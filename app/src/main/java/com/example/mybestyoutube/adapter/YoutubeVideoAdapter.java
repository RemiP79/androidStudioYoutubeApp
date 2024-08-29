package com.example.mybestyoutube.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybestyoutube.R;
import com.example.mybestyoutube.pojo.YoutubeVideo;

import java.util.List;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.YoutubeVideoViewHolder> {

    private List<YoutubeVideo> youtubeVideos;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(YoutubeVideo video);
    }
    public class YoutubeVideoViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitre;
        public TextView tvDescription;

        public YoutubeVideoViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            tvTitre=itemView.findViewById(R.id.tvTitre);
            tvDescription=itemView.findViewById(R.id.tvDescription);

            itemView.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener !=null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick((YoutubeVideo) v.getTag());
                    }
                }
            }));
        }
    }

    public YoutubeVideoAdapter(List<YoutubeVideo> youtubeVideos,OnItemClickListener listener){
        this.youtubeVideos = youtubeVideos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YoutubeVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.youtube_item_llo,parent,false);
        return new YoutubeVideoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(YoutubeVideoViewHolder holder, int position){
        YoutubeVideo youtubeVideo = youtubeVideos.get(position);
        holder.tvTitre.setText(youtubeVideo.getTitre());
        holder.tvDescription.setText(youtubeVideo.getDescription());
        holder.itemView.setTag(youtubeVideo);
        Log.d("YoutubeVideoAdapter", "Category: " + youtubeVideo.getCategorie());
    }

    @Override
    public int getItemCount() { return youtubeVideos.size();}
}
