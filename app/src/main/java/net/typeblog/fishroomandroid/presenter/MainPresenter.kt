package net.typeblog.fishroomandroid.presenter

import android.os.Bundle
import nucleus.presenter.RxPresenter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import net.typeblog.fishroomandroid.api.*
import net.typeblog.fishroomandroid.model.Message
import net.typeblog.fishroomandroid.view.MainActivity
import java.util.concurrent.TimeUnit
import kotlin.collections.MutableList

/**
 * Created by peter on 6/14/16.
 */
class MainPresenter: RxPresenter<MainActivity>() {
    val REQUEST_LOOP = 1
    val KEY_LIST = "${MainPresenter::class.qualifiedName}#LIST"
    val mService = Retrofit.Builder()
            .baseUrl(FISHROOM)
            .addConverterFactory(JacksonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(Api::class.java)
    var mList: MutableList<Message> = mutableListOf<Message>()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        if (savedState != null)
            mList = savedState.getParcelableArray(KEY_LIST).toMutableList() as MutableList<Message>

        restartableLatestCache(REQUEST_LOOP, {
            // Begin the long-polling operation
            Observable.interval(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        mService.getMessages(APP_ID.toString(), APP_KEY, "tuna-random")
                    }
                    .retry()
                    .flatMapIterable { it.messages }
                    .observeOn(AndroidSchedulers.mainThread())
        }, { activity, response ->
            mList.add(0, response)
            activity.onNewMessage()
        }, { activity, throwable ->
            throw throwable
        })
    }

    override fun onSave(state: Bundle) {
        super.onSave(state)
        state.putParcelableArray(KEY_LIST, mList.toTypedArray())
    }

    fun startLoop() {
        start(REQUEST_LOOP)
    }
}