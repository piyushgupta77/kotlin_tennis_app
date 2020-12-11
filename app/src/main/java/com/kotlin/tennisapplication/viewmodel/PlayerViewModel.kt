package com.kotlin.tennisapplication.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotlin.tennisapplication.Constant
import com.kotlin.tennisapplication.model.actions.PlayerActionGenerator
import com.kotlin.tennisapplication.model.actions.PlayerActionProcessor
import com.kotlin.tennisapplication.model.entity.PlayerEntity
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class PlayerViewModel @ViewModelInject constructor(
    private val playerActionGenerator: PlayerActionGenerator,
    private val playerActionProcessor: PlayerActionProcessor
) : ViewModel() {
    private val TAG: String = "PlayerViewModel"
    val isDeuce = MutableLiveData<Boolean>().apply { value = false }
    val hasAdvantage = MutableLiveData<PlayerEntity>()
    val winner = MutableLiveData<PlayerEntity>()

    private var keepRunning: Boolean = true
    private val lock = ReentrantLock()
    private val condition = lock.newCondition()
    private var flag: Int = 0
    lateinit var player1: PlayerEntity
    lateinit var player2: PlayerEntity
    private lateinit var playerAction: IPlayerAction

    fun setListener(playerAction: IPlayerAction){
        this.playerAction = playerAction
    }

    fun startGame() {
        keepRunning = true
        player1 = PlayerEntity(0, Runnable {
            while (keepRunning) {
                lock.withLock {
                    if (flag != player1.playerNumber) {
                        //wait of this player's turn
                        try {
                            condition.await()
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Interrupted " + player1.name)
                        }
                    }
                    if (keepRunning) {
                        Log.i(TAG, "Running " + player1.name)
                        val event: Int = playerActionGenerator.generatePlayerEvent()
                        postPlayerTurn(player1)
                        playerActionProcessor.processActionEvent(
                            playerAction = playerAction,
                            viewModel = this,
                            action = event,
                            currentPlayer = player1,
                            otherPlayer = player2
                        )
                        flag = player2.playerNumber
                        try {
                            Thread.sleep(Constant.PLAYER_ACTION_TIME_SIMULATION)
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Interrupted " + player2.name)
                        }
                    }
                    condition.signalAll()
                }
            }
        }, "Player1")

        player2 = PlayerEntity(1, Runnable {
            while (keepRunning) {
                lock.withLock {
                    if (flag != player2.playerNumber) {
                        //wait of this player's turn
                        try {
                            condition.await()
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Interrupted " + player2.name)
                        }
                    }
                    if (keepRunning) {
                        Log.i(TAG, "Running " + player2.name)
                        val event: Int = playerActionGenerator.generatePlayerEvent()
                        postPlayerTurn(player2)
                        playerActionProcessor.processActionEvent(
                            playerAction = playerAction,
                            viewModel = this,
                            action = event,
                            currentPlayer = player2,
                            otherPlayer = player1
                        )
                        flag = player1.playerNumber
                        try {
                            Thread.sleep(Constant.PLAYER_ACTION_TIME_SIMULATION)
                        } catch (e: InterruptedException) {
                            Log.e(TAG, "Interrupted " + player2.name)
                        }
                    }
                    condition.signalAll()
                }
            }
        }, "Player2")

        // start game for both players
        player1.start()
        player2.start()
    }

    private fun postPlayerTurn(currentPlayer:PlayerEntity) {
        // setting value using postValue() sometimes result in posting only
        // last value hence directly set value on UI thread
        playerAction.onAction(Constant.ACTION_PLAYER_TOOK_TURN, currentPlayer)
    }
    fun stopGame() {
        keepRunning = false
        player1.interrupt()
        player2.interrupt()
    }

    override fun onCleared() {
        super.onCleared()
        keepRunning = false
        player1.interrupt()
        player2.interrupt()
    }

    interface IPlayerAction{
        fun onAction(action:Int , player: PlayerEntity)
    }
}