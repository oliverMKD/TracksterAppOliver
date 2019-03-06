package com.trackster.tracksterapp.listeners

interface TotalListener {
    fun onTotalChanged(sum: Int)

    fun expandGroupEvent(groupPosition: Int, isExpanded: Boolean)
}