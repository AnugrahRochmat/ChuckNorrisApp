package com.anugrahrochmat.chuck.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.data.FactContract;
import com.anugrahrochmat.chuck.model.Result;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Result> results;
    private Context context;
    //private Result result;

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {
        public final TextView searchResult, numRes;
        public ImageButton shareBtn, favBtn;

        public SearchResultViewHolder(View view){
            super(view);
            searchResult = view.findViewById(R.id.search_result);
            shareBtn = view.findViewById(R.id.share_button);
            favBtn = view.findViewById(R.id.favourite_button);
            numRes = view.findViewById(R.id.number_result);

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setType("text/plain");
                    String shareText = "Did you know \"" + results.get(getAdapterPosition()).getValue() + "\"";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    context.startActivity(Intent.createChooser(shareIntent, "Share This With..."));
                }
            });

            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateFavourites(results.get(getAdapterPosition()));
                    if (isFavourite(results.get(getAdapterPosition()))){
                        favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else{
                        favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                }
            });
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
    public void onBindViewHolder(SearchResultAdapter.SearchResultViewHolder holder, final int position) {
        Result result = results.get(position);

        int resPosition = position + 1;
        String numOfResult = "#" + String.valueOf(resPosition);
        holder.searchResult.setText(result.getValue());
        holder.numRes.setText(numOfResult);

        if (isFavourite(result)){
            holder.favBtn.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    /**
     * updateFavourites method for click event
     */
    public void updateFavourites(Result result){
        if (!isFavourite(result)){
            addFavourites(result);
        } else {
            removeFavourites(result);
        }
    }

    public void addFavourites(Result result) {
        //Insert favourites
        ContentValues values = new ContentValues();
        values.put(FactContract.FactEntry.COLUMN_FACT_ID, result.getId());
        values.put(FactContract.FactEntry.COLUMN_FACT_URL, result.getUrl());
        values.put(FactContract.FactEntry.COLUMN_FACT_VALUE, result.getValue());

        context.getContentResolver().insert(FactContract.FactEntry.CONTENT_URI, values);
    }

    public void removeFavourites(Result result) {
        //Remove favourites
        Uri uri = FactContract.FactEntry.CONTENT_URI.buildUpon().appendEncodedPath(result.getId()).build();
        context.getContentResolver().delete(uri, null, null);
    }

    /**
     * isFavourite method
     */
    public boolean isFavourite(Result result){
        Uri uri = FactContract.FactEntry.CONTENT_URI.buildUpon().appendEncodedPath(result.getId()).build();
        Cursor cursor = context.getContentResolver().query(uri,
                null,
                FactContract.FactEntry.COLUMN_FACT_ID + " = ? ",
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    /**
     * add method to add cursor into adapter
     * @param cursor
     */
    public void add(Cursor cursor) {
        results.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String url =  cursor.getString(1);
                String value = cursor.getString(2);
                Result result= new Result(id, url, value);
                results.add(result);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

}
