package com.anugrahrochmat.chuck.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.activity.MainActivity;
import com.anugrahrochmat.chuck.model.RandomCategory;
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
    Button generateButton;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RandomCategory randomJoke;
    private String categoryName;
    private static final String DEF_TITLE = "Home";

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
                item.setIcon(R.drawable.ic_favorite_black_24dp);
                Toast.makeText(getActivity(), "Soon Favourite feature!", Toast.LENGTH_LONG).show();
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

        loadRandomJoke();

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRandomJoke();
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

    public class fetchRandomJoke extends AsyncTask<Void, Void, RandomCategory> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideJoke();
            showProgressBar();
        }

        @Override
        protected RandomCategory doInBackground(Void... voids) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<RandomCategory> call = apiService.getRandomCategory(categoryName);

            try {
                randomJoke = call.execute().body();
                return randomJoke;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(RandomCategory randomJoke) {
            hideProgressbar();
            showJoke();
            if (randomJoke != null){
                randomJokeView.setText(randomJoke.getValue());
            } else {
                showErrorMessage();
            }
        }
    }

    private void loadRandomJoke(){
        new fetchRandomJoke().execute();
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
}
