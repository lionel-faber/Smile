package me.lionelfaber.jsontest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LinkHolder> {




    private ArrayList<Link> linkArrayList;

    // 1
    public static class LinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;

        // 2
        private ImageView image;
        private TextView title;
        private TextView subtitle;
        private Link mLink;

        public void bindLink(Link link) {
            mLink = link;
            switch(Integer.parseInt(link.getType()))
            {
                case 1:
                    Glide.with(context).load(R.drawable.cn).into(image);
                    break;
                case 2:
                    Glide.with(context).load(R.drawable.qb).into(image);
                    break;
                case 3:
                    Glide.with(context).load(R.drawable.hw).into(image);
                    break;
                case 4:
                    Glide.with(context).load(R.drawable.tq).into(image);
                    break;
            }
            title.setText(link.getTitle());
            subtitle.setText(link.getLink());
        }

        // 3
        private static final String PHOTO_KEY = "PHOTO";

        // 4
        public LinkHolder(View v) {
            super(v);
            this.context = v.getContext();

            image = (ImageView) v.findViewById(R.id.icon);
            title = (TextView) v.findViewById(R.id.firstLine);
            subtitle = (TextView) v.findViewById(R.id.secondLine);
            final String s = subtitle.getText().toString();
            v.setOnClickListener(this);
        }

        // 5
        @Override
        public void onClick(View v) {

                    TextView link  = (TextView)v.findViewById(R.id.secondLine);
                    final String l = link.getText().toString();
                    Uri uri = Uri.parse(l);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
        }
    }


    public RecyclerAdapter(ArrayList<Link> links) {
        linkArrayList = links;
    }

    @Override
    public RecyclerAdapter.LinkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new LinkHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.LinkHolder holder, int position) {

        Link itemLink = linkArrayList.get(position);
        holder.bindLink(itemLink);

    }

    @Override
    public int getItemCount() {
        return linkArrayList.size();
    }
}