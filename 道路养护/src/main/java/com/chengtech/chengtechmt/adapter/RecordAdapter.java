package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.business.DiseaseVoiceRecord;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/9/14 13:59.
 * 展示录音列表的adapter
 */

public class RecordAdapter extends RecyclerView.Adapter {

    private List<DiseaseVoiceRecord> data;
    private Context mContext;


    public RecordAdapter(Context context, List<DiseaseVoiceRecord> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_list, null, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final DiseaseVoiceRecord audio = data.get(position);
        if (audio != null) {
            myViewHolder.recordLength.setText(audio.recordLength + "\"");
            myViewHolder.video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (new File("file://" + audio.recordPath).exists()) {
                            final MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setDataSource("file://" + audio.recordPath);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.release();
                                }
                            });
                        } else {
                            Toast.makeText(mContext, "文件已经被删除。", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView video;
        private TextView recordLength;
        private ImageButton delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            video = (CardView) itemView.findViewById(R.id.video);
            recordLength = (TextView) itemView.findViewById(R.id.videoLength);
            delete = (ImageButton) itemView.findViewById(R.id.deleteRecord);
        }
    }

}
