package com.anugrahrochmat.chuck.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.activity.MainActivity;
import com.anugrahrochmat.chuck.data.FactContract;
import com.anugrahrochmat.chuck.model.Result;
import com.anugrahrochmat.chuck.rest.ApiClient;
import com.anugrahrochmat.chuck.rest.ApiInterface;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CATEGORY_NAME = "CATEGORY_NAME";

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.random_joke)
    TextView randomJokeView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.generate_button)
    FloatingActionButton generateButton;

    private static final String TAG = MainActivity.class.getSimpleName();
    private Result randomJoke;
    private String categoryName;
    private static final String DEF_TITLE = "Random";
    private MenuItem favMenuItem;
    private AsyncTask loadJokeTask;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catName Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String catName) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_NAME, catName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        if (getArguments() != null) {
            categoryName = getArguments().getString(CATEGORY_NAME);
            setUppercaseforTitleBar(categoryName);
        } else {
            setUppercaseforTitleBar(DEF_TITLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_fav_menu, menu);
        favMenuItem = menu.findItem(R.id.favourite_button);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_button:
                // Share
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setType("text/plain");
                String shareText = "Did you know \"" + randomJoke.getValue() + "\"";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(shareIntent, "Share This With..."));
                break;
            case R.id.favourite_button:
                // Favourite
//                item.setIcon(R.drawable.ic_favorite_black_24dp);
//                Toast.makeText(getActivity(), "Soon Favourite feature!", Toast.LENGTH_LONG).show();
                updateFavourites();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //loadRandomJoke();
        loadJokeTask = new fetchRandomJoke().execute();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // loadRandomJoke();
                loadJokeTask = new fetchRandomJoke().execute();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        loadJokeTask.cancel(true);
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

    /**
     * Asyntask Fetching Random Joke
     */
    public class fetchRandomJoke extends AsyncTask<Void, Void, Result> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideJoke();
            showProgressBar();
        }

        @Override
        protected Result doInBackground(Void... voids) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<Result> call = apiService.getRandomCategory(categoryName);

            try {
                randomJoke = call.execute().body();
                return randomJoke;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Result randomJoke) {
            hideProgressbar();
            changeIcon();
            showJoke();
            if (randomJoke != null){
                randomJokeView.setText(randomJoke.getValue());
            } else {
                showErrorMessage();
            }
        }
    }

    private void showJoke() {
        randomJokeView.setVisibility(View.VISIBLE);
    }

    private void hideJoke(){
        randomJokeView.setText("");
        randomJokeView.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar(){
        progressBar.setVisibility(View.GONE);
    }

    private void showErrorMessage(){
        errorMessage.setVisibility(View.VISIBLE);
    }

    public void setUppercaseforTitleBar(String text) {
        if (text.length() > 0) {
            text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
        }
        ((MainActivity) getActivity()).setToolbarTitle(text);
    }

    /**
     * updateFavourites method for click event
     */
    public void updateFavourites(){
        if (!isFavourite()){
            addFavourite();
        } else {
            removeFavourites();
        }
    }

    /**
     * changeIcon method
     */
    public void changeIcon() {
        if (isFavourite()){
            favMenuItem.setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            favMenuItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    /**
     * isFavourite method
     */
    public boolean isFavourite(){
        Uri uri = FactContract.FactEntry.CONTENT_URI.buildUpon().appendEncodedPath(randomJoke.getId()).build();
        Cursor cursor = getActivity().getContentResolver().query(uri,
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
     * addFavourite Method
     */
    @SuppressLint("StaticFieldLeak")
    public void addFavourite(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                if (!isFavourite()) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(FactContract.FactEntry.COLUMN_FACT_ID, randomJoke.getId());
                    contentValues.put(FactContract.FactEntry.COLUMN_FACT_URL, randomJoke.getUrl());
                    contentValues.put(FactContract.FactEntry.COLUMN_FACT_VALUE, randomJoke.getValue());

                    getActivity().getContentResolver().insert(FactContract.FactEntry.CONTENT_URI, contentValues);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                changeIcon();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * removeFavourites Method
     */
    @SuppressLint("StaticFieldLeak")
    public void removeFavourites(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (isFavourite()) {
                    Uri uri = FactContract.FactEntry.CONTENT_URI.buildUpon().appendEncodedPath(randomJoke.getId()).build();
                    getActivity().getContentResolver().delete(uri, null, null);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                changeIcon();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
