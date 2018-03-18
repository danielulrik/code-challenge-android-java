package com.arctouch.codechallenge.viewmodel;

import android.graphics.drawable.Drawable;

import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Daniel Ulrik on 18/03/2018.
 */

public class MovieDetailsViewModel implements ViewModel {

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();
    private Movie movie;

    public MovieDetailsViewModel(Movie movie) {
        this.movie = movie;
    }

    public String getTitle() {
        return movie.title;
    }

    public String getGenres() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : movie.genres) {
            stringBuilder.append(genre.toString()).append(", ");
        }
        if (stringBuilder.length() < 3) {
            return stringBuilder.toString();
        }
        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
    }

    public String getOverview() {
        return movie.overview;
    }

    public String getReleaseDate() {
        return movie.releaseDate;
    }

    public String getPosterUrl() {
        return movieImageUrlBuilder.buildPosterUrl(movie.posterPath);
    }

    public Observable<Drawable> getPoster() {
        return Observable.fromCallable(() -> {
            try {
                InputStream is = (InputStream) new URL(movieImageUrlBuilder.buildPosterUrl(movie.posterPath)).getContent();
                Drawable d = Drawable.createFromStream(is, "");
                return d;
            } catch (Exception e) {
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Drawable> getBackgroundDrop() {
        return Observable.fromCallable(() -> {
            try {
                InputStream is = (InputStream) new URL(movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath)).getContent();
                Drawable d = Drawable.createFromStream(is, "");
                return d;
            } catch (Exception e) {
                return null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onResume() {
    }

    public String getBackdropUrl() {
        return movieImageUrlBuilder.buildBackdropUrl(movie.backdropPath);
    }
}
