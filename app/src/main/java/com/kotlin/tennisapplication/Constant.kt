package com.kotlin.tennisapplication

import androidx.annotation.IntDef

interface Constant {
    companion object {
        const val PLAYER_ACTION_TIME_SIMULATION =1000L
        const val DELAY_PLAYER_IMAGE_HIDE = 500L

        const val ACTION_HIT = 0
        const val ACTION_MISS = 1
        const val ACTION_GAINED_POINT = 2
        const val ACTION_PLAYER_TOOK_TURN = 3

        @IntDef(ACTION_HIT,ACTION_MISS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class PlayerActionEvent
    }
}