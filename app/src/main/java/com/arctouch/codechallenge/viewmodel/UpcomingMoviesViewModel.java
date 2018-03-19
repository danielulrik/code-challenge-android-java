package com.arctouch.codechallenge.viewmodel;

import com.arctouch.codechallenge.App;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.GenreResponse;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.model.UpcomingMoviesResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Daniel Ulrik on 17/03/2018.
 */

public class UpcomingMoviesViewModel implements ViewModel {

    private Observable<GenreResponse> cacheGenre() {
        return Observable.defer(() -> {
            if (!Cache.getGenres().isEmpty()) {
                return Observable.just(new GenreResponse(Cache.getGenres()));
            }
            return App.getApi().genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        });
    }

    public Observable<List<Movie>> getUpcomingMovies(long page) {
        return Observable.combineLatest(cacheGenre(), App.getApi().upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION), (genreResponse, upcomingMoviesResponse) -> {
            Cache.setGenres(genreResponse.genres);
            for (Movie movie : upcomingMoviesResponse.results) {
                movie.genres = new ArrayList<>();
                for (Genre genre : Cache.getGenres()) {
                    if (movie.genreIds.contains(genre.id)) {
                        movie.genres.add(genre);
                    }
                }
            }
            return upcomingMoviesResponse.results;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onResume() {
    }

}
