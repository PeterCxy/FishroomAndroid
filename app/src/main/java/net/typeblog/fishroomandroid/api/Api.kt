package net.typeblog.fishroomandroid.api

import retrofit2.*
import retrofit2.http.*
import rx.Observable
import net.typeblog.fishroomandroid.model.*

/**
 * Created by peter on 6/14/16.
 */
interface Api {
    @GET("/api/messages")
    fun getMessages(@Query("room") room: String): Observable<MessageList>

    @POST("/api/messages/{room}/")
    fun sendMessage(@Path("room") room: String, @Body body: SendMessageRequestBody): Observable<SendMessageResponseBody>
}