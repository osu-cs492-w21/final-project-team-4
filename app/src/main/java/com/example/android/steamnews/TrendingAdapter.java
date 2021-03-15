package com.example.android.steamnews;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.TrendingDataItem;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {
    private final static String TAG = TrendingAdapter.class.getSimpleName();
    private List<TrendingDataItem> searchResultsList;
    private TrendingAdapter.OnSearchResultClickListener resultClickListener;

    interface OnSearchResultClickListener {
        void onTitleResultClicked(TrendingDataItem gameAppidItem);
    }

    public TrendingAdapter(TrendingAdapter.OnSearchResultClickListener listener) {
        Log.d(TAG, "trendadat");
        this.resultClickListener = (TrendingAdapter.OnSearchResultClickListener) listener;
    }

    public void updateSearchResults(List<TrendingDataItem> searchResultsList) {
        this.searchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.searchResultsList != null) {
            return this.searchResultsList.size();
        } else {
            return 0;
        }
    }
    @NonNull
    @Override
    public TrendingAdapter.TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        Log.d(TAG, "inflate");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_article_item, parent, false);
        return new TrendingAdapter.TrendingViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull TrendingAdapter.TrendingViewHolder holder, int position) {
        Log.d(TAG, "Binding: " + String.valueOf(this.searchResultsList.get(position)));
        holder.bind(this.searchResultsList.get(position));
    }

    class TrendingViewHolder extends RecyclerView.ViewHolder {
        private TextView searchResultTV;

        TrendingViewHolder(View itemView) {
            super(itemView);
            this.searchResultTV = itemView.findViewById(R.id.tv_game_search_result);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resultClickListener.onTitleResultClicked(
                            searchResultsList.get(getAdapterPosition())
                    );
                }
            });
        }

        void bind(TrendingDataItem gameAppidItem) {
            this.searchResultTV.setText(gameAppidItem.name);
            searchResultTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }
    }
}
