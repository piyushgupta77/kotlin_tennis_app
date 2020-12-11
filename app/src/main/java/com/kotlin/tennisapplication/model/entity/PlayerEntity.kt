package com.kotlin.tennisapplication.model.entity

class PlayerEntity(var playerNumber: Int, runnable: Runnable, name: String) :
    Thread(runnable, name) {
    var hitCount = 0
    var missCount = 0
    var totalPoints = 0
    var isOnAdvantage = false

    override fun toString(): String {
        return "$name hitCount $hitCount missCount $missCount totalPoints $totalPoints"
    }
}