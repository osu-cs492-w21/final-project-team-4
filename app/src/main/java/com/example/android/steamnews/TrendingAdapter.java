package com.example.android.steamnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.TrendingDataItem;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder> {
    private List<TrendingDataItem> searchResultsList;
    private TrendingAdapter.OnSearchResultClickListener resultClickListener;

    interface OnSearchResultClickListener {
        void onSearchResultClicked(TrendingDataItem articleDataItem);
    }

    public TrendingAdapter(TrendingAdapter.OnSearchResultClickListener listener) {
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_article_item, parent, false);
        return new TrendingAdapter.TrendingViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull TrendingAdapter.TrendingViewHolder holder, int position) {
        holder.bind(this.searchResultsList.get(position));
    }

    class TrendingViewHolder extends RecyclerView.ViewHolder{
        private TextView nameTV;
        private TextView urlTV;

        TrendingViewHolder(View itemView){
            super(itemView);
            this.nameTV= itemView.findViewById(R.id.tv_article_title);
            //this.urlTV = itemView.findViewById(R.id.tv_article_url);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    resultClickListener.onSearchResultClicked(
                            searchResultsList.get(getAdapterPosition())
                    );
                }
            });
        }
        void bind (TrendingDataItem articleDataItem){
            this.nameTV.setText(articleDataItem.appID);
            // this.urlTV.setText(articleDataItem.url);

        }
    }
}
