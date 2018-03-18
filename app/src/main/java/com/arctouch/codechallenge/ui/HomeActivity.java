package com.arctouch.codechallenge.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

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
    private int currentPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        ButterKnife.bind(this);
        configRecycler();
        loadUpcomingMoviews(currentPage);
    }

    private void configRecycler() {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems = manager.getChildCount();
                int totalItems = manager.getItemCount();
                int scrollOutItems = manager.findFirstVisibleItemPosition();

                if (currentItems + scrollOutItems == totalItems) {
                    loadUpcomingMoviews(currentPage++);
                }
            }
        });
    }

    private void loadUpcomingMoviews(long page) {
        disposableObserver = viewModel.getUpcomingMovies(page).subscribeWith(new DisposableObserver<List<Movie>>() {
            @Override
            public void onNext(List<Movie> movies) {
                if (movies.isEmpty()) {
                    currentPage = 1;
                } else {
                    recyclerView.setAdapter(new HomeAdapter(movies));
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace(); // todo treat error correctly
            }

            @Override
            public void onComplete() {
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
}
