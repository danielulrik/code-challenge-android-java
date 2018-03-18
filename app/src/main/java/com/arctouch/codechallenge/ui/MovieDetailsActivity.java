package com.arctouch.codechallenge.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.viewmodel.MovieDetailsViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.backDropImageView)
    ImageView backDropImageView;
    @BindView(R.id.posterImageView)
    ImageView posterImageView;
    @BindView(R.id.movieTitleTextView)
    TextView movieTitleTextView;
    @BindView(R.id.releaseDateTextView)
    TextView releaseDateTextView;
    @BindView(R.id.genresTextView)
    TextView genresTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;

    private MovieDetailsViewModel viewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        viewModel = new MovieDetailsViewModel(movie);
        ab.setTitle(viewModel.getTitle());
        loadDetails();
    }

    private void loadDetails() {
        loadPoster();
        loadBackdrop();
        movieTitleTextView.setText(viewModel.getTitle());
        genresTextView.setText(viewModel.getGenres());
        overviewTextView.setText(viewModel.getOverview());
        overviewTextView.setMovementMethod(new ScrollingMovementMethod());
        releaseDateTextView.setText(viewModel.getReleaseDate());
    }

    private void loadPoster() {
        Glide.with(MovieDetailsActivity.this)
                .load(viewModel.getPosterUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(posterImageView);
    }

    private void loadBackdrop() {
        Glide.with(MovieDetailsActivity.this)
                .load(viewModel.getBackdropUrl())
                .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(backDropImageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
