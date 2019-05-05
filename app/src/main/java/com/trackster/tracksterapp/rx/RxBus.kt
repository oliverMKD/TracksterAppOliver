package com.trackster.tracksterapp.rx

import io.reactivex.subjects.PublishSubject

object RxBus {

    private val publisher = PublishSubject.create<Any>()

    fun publish(event: Any) {
        publisher.onNext(event)
    }

    // Listen returns an Observable
    // Using ofType we filter only events that match that class type
    fun <T> listen(eventType: Class<T>) = publisher.ofType(eventType)!!
}