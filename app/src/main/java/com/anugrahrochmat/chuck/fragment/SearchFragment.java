package com.anugrahrochmat.chuck.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.activity.MainActivity;
import com.anugrahrochmat.chuck.adapter.SearchResultAdapter;
import com.anugrahrochmat.chuck.model.Result;
import com.anugrahrochmat.chuck.model.SearchResult;
import com.anugrahrochmat.chuck.rest.ApiClient;
import com.anugrahrochmat.chuck.rest.ApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SearchFragment extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.search_result_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_query)
    EditText searchQuery;

    @BindView(R.id.search_button)
    ImageButton searchButton;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.error_message)
    TextView errorMessage;

    private String query;
    private SearchResultAdapter searchResultAdapter;
    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRV();

        // Handle Search Button CLick listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check search query empty string or not
                if (searchQuery.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Insert query to search", Toast.LENGTH_SHORT).show();
                }else{
                    loadSearchResult(searchQuery.getText().toString());
                }
            }
        });

        // Handle search query enter pressed equal to button being clicked
        searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);

                    searchButton.performClick();
                    return true;
                }
                return false;
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


    public class FetchSearchResult extends AsyncTask<Void, Void, List<Result>> {

        private String searchQuery;

        public FetchSearchResult(String query){
            this.searchQuery = query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideResult();
            showProgressBar();
        }

        @Override
        protected List<Result> doInBackground(Void... voids) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<SearchResult> call = apiService.getSearchQuery(searchQuery);

            try {
                SearchResult searchResult = call.execute().body();
                List<Result> results = searchResult.getResult();
                return results;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Result> results) {
            hideProgressbar();
            showResult();
            if (results != null) {
                searchResultAdapter.setResults(results);
            } else {
                showErrorMessage();
            }
        }
    }

    public void initRV(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        searchResultAdapter = new SearchResultAdapter(new ArrayList<Result>(), getContext());
        recyclerView.setAdapter(searchResultAdapter);
    }

    private void loadSearchResult(String query){
        new FetchSearchResult(query).execute();
    }

    private void showResult(){
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideResult(){
        recyclerView.setVisibility(View.INVISIBLE);
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

}
