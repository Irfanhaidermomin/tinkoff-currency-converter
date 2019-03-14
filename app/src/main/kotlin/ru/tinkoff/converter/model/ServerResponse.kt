package ru.tinkoff.converter.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServerResponse<T>(
    @SerializedName("results")
    val results: Map<String, T>
) : Serializable
