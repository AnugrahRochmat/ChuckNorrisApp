package com.anugrahrochmat.chuck.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.model.Result;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Result> results;
    private Context context;

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public final TextView searchResult;

        public SearchResultViewHolder(View view){
            super(view);
            searchResult = view.findViewById(R.id.search_result);
        }
    }

    public SearchResultAdapter(List<Result> results, Context context){
        this.results = results;
        this.context = context;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutInflater = R.layout.list_search_result;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutInflater, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultAdapter.SearchResultViewHolder holder, int position) {
        Result result = results.get(position);
        holder.searchResult.setText(result.getValue());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }
}
