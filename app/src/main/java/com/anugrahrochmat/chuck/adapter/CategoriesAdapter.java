package com.anugrahrochmat.chuck.adapter;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.fragment.HomeFragment;
import com.anugrahrochmat.chuck.view.CapitalizedTextView;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<String> categories;
    private Context context;

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final CapitalizedTextView catName;

        public CategoriesViewHolder(View view) {
            super(view);
            catName = view.findViewById(R.id.category_name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String category = categories.get(getAdapterPosition());

            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            HomeFragment fragment = HomeFragment.newInstance(category);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commitAllowingStateLoss();
        }
    }

    public CategoriesAdapter(List<String>  categories, Context context){
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
    public void onBindViewHolder(final CategoriesViewHolder holder, final int position) {
        String category = categories.get(position);
        holder.catName.setText(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<String> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    public List<String> getCategories() {
        return categories;
    }

}
