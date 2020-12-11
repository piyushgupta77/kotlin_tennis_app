package com.kotlin.tennisapplication.model.points

import com.kotlin.tennisapplication.Constant
import com.kotlin.tennisapplication.model.entity.PlayerEntity
import com.kotlin.tennisapplication.viewmodel.PlayerViewModel

class PointsProcessor {

    fun  processScore(
        playerAction: PlayerViewModel.IPlayerAction,
        viewModel: PlayerViewModel,
        currentPlayer: PlayerEntity,
        otherPlayer: PlayerEntity
    ) {
        if (currentPlayer.totalPoints < 3 && otherPlayer.totalPoints < 3) {
            //normal
            currentPlayer.totalPoints++
            playerAction.onAction(Constant.ACTION_GAINED_POINT, currentPlayer)
        } else if ((currentPlayer.totalPoints == 3 && currentPlayer.totalPoints > otherPlayer.totalPoints)
            || (currentPlayer.totalPoints == 3 && currentPlayer.isOnAdvantage)
        ) {
            //win current
            viewModel.winner.postValue(currentPlayer)
        } else if ((currentPlayer.totalPoints == 2 && otherPlayer.totalPoints == 3)) {
            // deuce
            currentPlayer.totalPoints++
            viewModel.isDeuce.postValue(true)
        } else if (currentPlayer.totalPoints == 3 && otherPlayer.totalPoints == 3
            && !currentPlayer.isOnAdvantage && !otherPlayer.isOnAdvantage
        ) {
            //cp on advantage
            currentPlayer.isOnAdvantage =true
            viewModel.hasAdvantage.postValue(currentPlayer)
        } else if (currentPlayer.totalPoints == 3
            && !currentPlayer.isOnAdvantage && otherPlayer.isOnAdvantage
        ) {
            // deuce
            currentPlayer.isOnAdvantage = false
            otherPlayer.isOnAdvantage = false
            viewModel.isDeuce.postValue(true)
        }
    }
}