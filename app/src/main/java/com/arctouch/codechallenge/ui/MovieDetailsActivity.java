package com.arctouch.codechallenge.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.viewmodel.MovieDetailsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.backgroundLinearLayout)
    RelativeLayout backgroundLinearLayout;
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
        compositeDisposable.add(loadPoster());
        compositeDisposable.add(loadBackground());
        movieTitleTextView.setText(viewModel.getTitle());
        genresTextView.setText(viewModel.getGenres());
        overviewTextView.setText(viewModel.getOverview());
        overviewTextView.setMovementMethod(new ScrollingMovementMethod());
        releaseDateTextView.setText(viewModel.getReleaseDate());
    }

    @NonNull
    private DisposableObserver<Drawable> loadPoster() {
        return viewModel.getPoster().subscribeWith(new DisposableObserver<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                posterImageView.setBackground(drawable);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @NonNull
    private DisposableObserver<Drawable> loadBackground() {
        return viewModel.getBackgroundDrop().subscribeWith(new DisposableObserver<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                backgroundLinearLayout.setBackground(drawable);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
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
