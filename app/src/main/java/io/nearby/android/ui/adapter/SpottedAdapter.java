package io.nearby.android.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import io.nearby.android.R;
import io.nearby.android.data.Spotted;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class SpottedAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private List<Spotted> mDataset;
    private RequestManager mGlide;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(Spotted spotted);
    }

    public class SpottedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView messageTextView;
        private ImageView pictureImageView;

        public SpottedViewHolder(View v) {
            super(v);
            messageTextView = v.findViewById(R.id.spotted_message);
            pictureImageView = v.findViewById(R.id.spotted_picture);
        }

        public void bind(String message, String thumbnailUrl){
            messageTextView.setText(message);

            if (thumbnailUrl == null) {
                Glide.clear(pictureImageView);
                pictureImageView.setVisibility(View.GONE);
            } else {
                pictureImageView.setVisibility(View.VISIBLE);
                mGlide.load(thumbnailUrl)
                        .into(pictureImageView);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                Spotted spotted = mDataset.get(position);
                onItemClickListener.onItemClick(spotted);
            }
        }
    }

    public SpottedAdapter(RequestManager glide) {
        mDataset = new ArrayList<>();
        mGlide = glide;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spotted_card, parent, false);
        return new SpottedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Spotted spotted = mDataset.get(position);
        SpottedViewHolder spottedViewHolder = (SpottedViewHolder) holder;

        spottedViewHolder.bind(spotted.getMessage(),
                spotted.getThumbnailURL());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Spotted getItem(int position) {
        Spotted spotted = null;
        if (position >= 0 && position < mDataset.size()) {
            spotted = mDataset.get(position);
        }
        return spotted;
    }

    public void addItem(Spotted spotted) {
        if (!mDataset.contains(spotted)) {
            mDataset.add(spotted);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<Spotted> spotteds) {
        for (Spotted spotted : spotteds) {
            if (!mDataset.contains(spotted)) {
                mDataset.add(spotted);
            }
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void insert(Spotted spotted) {
        if (!mDataset.contains(spotted)) {
            mDataset.add(0, spotted);
            notifyDataSetChanged();
        }
    }

    public void insertAll(List<Spotted> spotteds) {
        for (int i = spotteds.size() - 1; i > 0; i--) {
            Spotted spotted = spotteds.get(i);
            if (!mDataset.contains(spotted)) {
                mDataset.add(0, spotted);
            }
        }

        notifyDataSetChanged();
    }
}