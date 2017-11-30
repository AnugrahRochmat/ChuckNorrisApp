package com.anugrahrochmat.chuck.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.activity.MainActivity;
import com.anugrahrochmat.chuck.adapter.CategoriesAdapter;
import com.anugrahrochmat.chuck.rest.ApiClient;
import com.anugrahrochmat.chuck.rest.ApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_CATEGORIES = "SAVED_CATEGORIES";

    @BindView(R.id.categories_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CategoriesAdapter categoriesAdapter;
    private OnFragmentInteractionListener mListener;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initRV();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_CATEGORIES)) {
                List<String> categoriesSaved = savedInstanceState.getStringArrayList(SAVED_CATEGORIES);
                categoriesAdapter.setCategories(categoriesSaved);
            }
        } else {
            new FetchCategoriesTask().execute();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> categoriesSaved = new ArrayList<>(categoriesAdapter.getCategories());
        if (categoriesSaved != null && !categoriesSaved.isEmpty()) {
            outState.putStringArrayList(SAVED_CATEGORIES, categoriesSaved);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // initialize recycler view
    public void initRV(){
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        categoriesAdapter = new CategoriesAdapter(new ArrayList<String>(), getContext());
        recyclerView.setAdapter(categoriesAdapter);
    }

    /**
     * FetchCategories from API
     */
    public class FetchCategoriesTask extends AsyncTask<Void, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            retrofit2.Call<List<String>> call = apiService.getCategoryList();

            try {
                List<String> categories = call.execute().body();
                return categories;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> categories) {
            hideProgressbar();
            categoriesAdapter.setCategories(categories);
        }
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar(){
        progressBar.setVisibility(View.GONE);
    }

}
