package com.arctouch.codechallenge.viewmodel;

import com.arctouch.codechallenge.App;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Daniel Ulrik on 17/03/2018.
 */

public class UpcomingMoviesViewModel implements ViewModel {

    public Observable<List<Movie>> getUpcomingMovies(long page) {
        return App.getApi().upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(upcomingMoviesResponse -> {
                    for (Movie movie : upcomingMoviesResponse.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    return Observable.fromCallable(() -> upcomingMoviesResponse.results);
                });
    }

    @Override
    public void onResume() {
    }

}
