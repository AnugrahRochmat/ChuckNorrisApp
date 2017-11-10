package com.anugrahrochmat.chuck.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.view.CapitalizedTextView;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>{

    private ArrayList<String> categories;
    private Context context;

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public final CapitalizedTextView catName;

        public CategoriesViewHolder(View view) {
            super(view);
            catName = view.findViewById(R.id.category_name);
        }
    }


    public CategoriesAdapter(ArrayList<String> categories, Context context){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutInflater = R.layout.list_categories;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutInflater, viewGroup, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, final int position) {
        String Category = categories.get(position);
        holder.catName.setText(Category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(ArrayList<String> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

}
