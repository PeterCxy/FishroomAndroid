package net.typeblog.fishroomandroid.presenter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import nucleus.presenter.RxPresenter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.Observable
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import net.typeblog.fishroomandroid.api.*
import net.typeblog.fishroomandroid.model.Message
import net.typeblog.fishroomandroid.model.SendMessageRequestBody
import net.typeblog.fishroomandroid.view.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import kotlin.collections.MutableList

/**
 * Created by peter on 6/14/16.
 */
class MainPresenter: RxPresenter<MainActivity>() {
    val REQUEST_LOOP = 1
    val KEY_LIST = "${MainPresenter::class.qualifiedName}#LIST"
    lateinit var mService: Api
    var mList: MutableList<Message> = mutableListOf<Message>()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        if (savedState != null)
            mList = savedState.getParcelableArray(KEY_LIST).toMutableList() as MutableList<Message>

        val httpClient = OkHttpClient().newBuilder()
        val interceptor = Interceptor() {
            chain ->
            val request = chain?.request()?.newBuilder()
                    ?.addHeader("Accept", "Application/JSON")
                    ?.addHeader("X-TOKEN-ID", APP_ID.toString())
                    ?.addHeader("X-TOKEN-KEY", APP_KEY)
                    ?.build()
            chain?.proceed(request)
        }
        httpClient.networkInterceptors().add(interceptor)
        mService = Retrofit.Builder()
                .baseUrl(FISHROOM)
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(Api::class.java)

        restartableLatestCache(REQUEST_LOOP, {
            // Begin the long-polling operation
            Observable.interval(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .flatMap {
                        mService.getMessages("tuna-random")
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

    fun sendMessage(body: SendMessageRequestBody) {

        mService.sendMessage("tuna-random", body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({result ->
                                view!!.sendResult(result.message == "OK")
                            },
                            {err ->
                                Log.e("NetWork", err.message)
                                err.printStackTrace()
                                view!!.sendResult(false)
                            })

    }
}