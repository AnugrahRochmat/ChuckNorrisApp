package com.anugrahrochmat.chuck.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anugrahrochmat.chuck.R;
import com.anugrahrochmat.chuck.model.RandomCategory;
import com.anugrahrochmat.chuck.rest.ApiClient;
import com.anugrahrochmat.chuck.rest.ApiInterface;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class EachCategoryActivity extends AppCompatActivity {

    @BindView(R.id.random_joke)
    TextView randomJokeView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.error_message)
    TextView errorMessage;

    @BindView(R.id.generate_button)
    Button generateButton;

    private static final String TAG = MainActivity.class.getSimpleName();
    private RandomCategory randomCategory;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_category);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            category = data.getString("category");
            setUppercaseforTitleBar(category);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        loadRandomJoke(category);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRandomJoke(category);
            }
        });
    }

    public void setUppercaseforTitleBar(String text) {
        if (text.length() > 0) {
            text = String.valueOf(text.charAt(0)).toUpperCase() + text.subSequence(1, text.length());
        }
        setTitle(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class fetchRandomJoke extends AsyncTask<Void, Void, RandomCategory> {

        private String category_name;

        public fetchRandomJoke(String cat_name) {
            category_name = cat_name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideJoke();
            showProgressBar();
        }

        @Override
        protected RandomCategory doInBackground(Void... voids) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<RandomCategory> call = apiService.getRandomCategory(category_name);

            try {
                randomCategory = call.execute().body();
                return randomCategory;
            } catch (IOException e) {
                Log.e(TAG, "A problem occured ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(RandomCategory randomCategory) {
            hideProgressbar();
            showJoke();
            if (randomCategory != null){
                randomJokeView.setText(randomCategory.getValue());
            } else {
                showErrorMessage();
            }
        }
    }

    private void showJoke(){
        randomJokeView.setVisibility(View.VISIBLE);
    }

    private void hideJoke(){
        randomJokeView.setText("");
        randomJokeView.setVisibility(View.INVISIBLE);
    }

    private void loadRandomJoke(String category){
        new fetchRandomJoke(category).execute();
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
