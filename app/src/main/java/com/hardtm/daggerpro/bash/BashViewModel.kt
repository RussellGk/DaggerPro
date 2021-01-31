package com.hardtm.daggerpro.bash

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hardtm.daggerpro.db.BashDao
import com.hardtm.daggerpro.db.BashEntity
import com.hardtm.daggerpro.db.DaggerProDatabase
import com.hardtm.daggerpro.rest.BashAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class BashViewModel(application: Application, bashApi: BashAPI) : AndroidViewModel(application) {

    private var disposables = CompositeDisposable()
    private val bashDao: BashDao
    private val injectionBashApi: BashAPI
    val bashList: LiveData<List<BashEntity>>
    val defaultProgressLiveData = MutableLiveData<Boolean>()

    init {
        defaultProgressLiveData.value = false // setValue -> on UI Thread, postValue -> from Background Thread
        bashDao = DaggerProDatabase.getDatabase(application).bashDao()
        bashList = bashDao.getBashList()
        injectionBashApi = bashApi
    }

    fun getBashData() {
        defaultProgressLiveData.value = true
        val disposable = injectionBashApi.getBash("bash.im", "bash", "16")
            .subscribeOn(Schedulers.io())
            .map { bashResponse ->
                if (bashResponse.isSuccessful) {
                    val bashList = bashResponse.body()
                    if (!bashList.isNullOrEmpty()) {
                        val bash = mutableListOf<BashEntity>()
                        bashList.forEachIndexed { index, bashStory ->
                            if (index != 0) {
                                bash.add(
                                    BashEntity(
                                        index, HtmlCompat.fromHtml(
                                            bashStory.elementPureHtml.toString(),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                        ).toString()
                                    )
                                )
                            }
                        }
                        bashDao.saveBashList(bash)
                    }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { defaultProgressLiveData.value = false }
            .subscribe({}, { throwableError ->
                Toast.makeText(getApplication(), throwableError.message, Toast.LENGTH_LONG).show()
                Log.d("BASHVIEWMODEL", throwableError.message.toString())
            })
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}