package net.typeblog.fishroomandroid.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by Harry Chen on 2016/6/23.
 */

data class SendMessageRequestBody(var nickName:String?, var content:String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SendMessageResponseBody(var message:String? = "")
