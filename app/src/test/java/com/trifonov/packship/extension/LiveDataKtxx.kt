package com.trifonov.packship.extension


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.trifonov.packship.util.SingleLiveEvent
import kotlin.reflect.KProperty0

fun <T> SingleLiveEvent<T>.verifyCalled() {
    verify(null)
}

fun <T> LiveData<T>.verify(value: T) {
    val observer: Observer<T> = mock()

    observeForever(observer)

    verify(observer).onChanged(value)

    removeObserver(observer)

}

fun <T> LiveData<T>.withObserver(block: (Observer<T>) -> Unit) {
    val observer: Observer<T> = mock()

    observeForever(observer)

    block(observer)

    removeObserver(observer)
}

fun <T> LiveData<T>.neverCalled() {
    val observer: Observer<T> = mock()

    observeForever(observer)

    verifyNoMoreInteractions(observer)

    removeObserver(observer)
}