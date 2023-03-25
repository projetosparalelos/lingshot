package com.language.teachermetoon.core.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.reflect.KProperty

typealias LiveResource<T> = LiveData<Resource<T>>
typealias MutableLiveResource<T> = MutableLiveData<Resource<T>>

sealed class Resource<T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val statusCode: Int?) : Resource<T>()
    class Loading<T> : Resource<T>()

    companion object {
        fun <T> success(data: T?): Resource<T> = Success(data)
        fun <T> error(statusCode: Int?): Resource<T> = Error(statusCode)
        fun <T> loading(): Resource<T> = Loading()
    }
}

private lateinit var lifecycleOwner: LifecycleOwner

fun <T> LiveResource<T>.setLifecycleOwner(owner: LifecycleOwner): LiveResource<T> {
    lifecycleOwner = owner
    return this
}

fun <T> LiveResource<T>.observeOnSuccess(observer: (T) -> Unit): LiveResource<T> {
    observe(lifecycleOwner) { resource ->
        if (resource is Resource.Success) {
            resource.data?.let { observer.invoke(it) }
        }
    }
    return this
}

fun <T> LiveResource<T>.observeOnError(observer: (Int) -> Unit): LiveResource<T> {
    observe(lifecycleOwner) { resource ->
        if (resource is Resource.Error) {
            resource.statusCode?.let { observer.invoke(it) }
        }
    }
    return this
}

fun <T> LiveResource<T>.observeOnLoading(observer: () -> Unit): LiveResource<T> {
    observe(lifecycleOwner) { resource ->
        if (resource is Resource.Loading) {
            observer.invoke()
        }
    }
    return this
}

fun <T> MutableLiveResource<T>.success(data: T?) {
    value = Resource.success(data)
}

fun <T> MutableLiveResource<T>.error(statusCode: Int?) {
    value = Resource.error(statusCode)
}

fun <T> MutableLiveResource<T>.loading() {
    postValue(Resource.loading())
}

fun <T> CoroutineScope.launchResource(
    mutableLiveResource: MutableLiveResource<T>,
    data: suspend () -> T?,
    onSuccess: suspend (T?) -> Unit = {},
    onError: suspend (Int?, String) -> Unit = { _, _ -> }
) {
    mutableLiveResource.loading()

    launch {
        mutableLiveResource.setupValidationState(data, onSuccess, onError)
    }
}

private suspend fun <T> MutableLiveResource<T>.setupValidationState(
    data: suspend () -> T?,
    onSuccess: suspend (T?) -> Unit = {},
    onError: suspend (Int, String) -> Unit = { _, _ -> }
) {
    try {
        data.invoke().also {
            this.success(it)
            onSuccess.invoke(it)
        }
    } catch (e: HttpException) {
        this.error(e.code())
        onError.invoke(e.code(), e.message())
    } catch (e: Exception) {
        this.error(0)
        onError.invoke(0, "")
    }
}

class GetLiveData<T>(private val data: MutableLiveData<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): LiveData<T> {
        return data
    }
}

fun <T> getLiveData(data: MutableLiveData<T>): GetLiveData<T> {
    return GetLiveData(data)
}