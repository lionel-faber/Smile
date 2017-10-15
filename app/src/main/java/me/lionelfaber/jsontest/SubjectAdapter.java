package me.lionelfaber.jsontest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by lionel on 15/10/17.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private Context mContext;
    private List<Subject> subjectList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, code;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            code = (TextView) view.findViewById(R.id.code);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public SubjectAdapter(Context mContext, List<Subject> subjectList) {
        this.mContext = mContext;
        this.subjectList = subjectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Subject album = subjectList.get(position);
        holder.title.setText(album.getName());
        holder.code.setText(album.getCode());

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent web = new Intent(mContext,SQLActivity.class);
                web.putExtra("SUB",album.getCode());
                web.putExtra("TITLE",album.getName());
                mContext.startActivity(web);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent web = new Intent(mContext,SQLActivity.class);
                web.putExtra("SUB",album.getCode());
                web.putExtra("TITLE",album.getName());
                mContext.startActivity(web);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

}
