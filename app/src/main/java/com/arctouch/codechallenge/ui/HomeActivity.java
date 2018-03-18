package com.arctouch.codechallenge.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.viewmodel.UpcomingMoviesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.observers.DisposableObserver;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @NonNull
    private UpcomingMoviesViewModel viewModel = new UpcomingMoviesViewModel();
    private DisposableObserver<List<Movie>> disposableObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        configRecycler();
        loadUpcomingMoviews(1);
    }

    private void configRecycler() {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getBaseContext(), "page. " + page, Toast.LENGTH_SHORT).show();
                //loadUpcomingMoviews(page);
            }
        });
    }

    private void loadUpcomingMoviews(long page) {
        disposableObserver = viewModel.getUpcomingMovies(page).subscribeWith(new DisposableObserver<List<Movie>>() {
            HomeAdapter adapter;
            @Override
            public void onNext(List<Movie> movies) {
                recyclerView.setAdapter(adapter = new HomeAdapter(movies));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(); // todo treat error correctly
            }

            @Override
            public void onComplete() {
                adapter.setOnItemClicked(viewClicked -> {
                    Movie movie = adapter.getItem(recyclerView.getChildLayoutPosition(viewClicked));
                    goToMovieDetails(movie);
                });
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposableObserver != null && !disposableObserver.isDisposed()) {
            disposableObserver.dispose();
        }
    }

    private void goToMovieDetails(Movie movie) {
        if (movie != null) {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        }
    }
}
