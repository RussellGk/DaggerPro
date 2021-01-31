package com.hardtm.daggerpro.jokes

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.hardtm.daggerpro.db.*
import com.hardtm.daggerpro.rest.JokeAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JokeViewModel(application: Application, jokeApi: JokeAPI) : AndroidViewModel(application) {

    private var disposables = CompositeDisposable()
    private val jokeDao: JokeDao
    private val injectionJokeApi: JokeAPI
    val jokeList: LiveData<List<JokeEntity>>

    init {
        jokeDao = DaggerProDatabase.getDatabase(application).jokeDao()
        jokeList = jokeDao.getJokeList()
        injectionJokeApi = jokeApi
    }

    fun getJokeData() {
        val disposable = injectionJokeApi.getJokes("anekdot.ru", "new anekdot", "15")
            .subscribeOn(Schedulers.io())
            .map { jokeResponse ->
                if (jokeResponse.isSuccessful) {
                    val jokeList = jokeResponse.body()
                    if (!jokeList.isNullOrEmpty()) {
                        val jokes = mutableListOf<JokeEntity>()
                        jokeList.forEachIndexed { index, jokeStory ->
                            if (index != 0) {
                                jokes.add(
                                    JokeEntity(
                                        index, HtmlCompat.fromHtml(
                                            jokeStory.elementPureHtml.toString(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                        ).toString()
                                    )
                                )
                            }
                        }
                        jokeDao.saveJokeList(jokes)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { throwableError ->
                Toast.makeText(getApplication(), throwableError.message, Toast.LENGTH_LONG).show()
                Log.d("JOKEVIEWMODEL", throwableError.message.toString())
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}