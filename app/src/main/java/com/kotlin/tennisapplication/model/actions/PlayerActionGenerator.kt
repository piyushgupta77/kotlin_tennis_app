package com.kotlin.tennisapplication.model.actions


/**
 * In real world Tennis game when a player receives a ball then he/she can either hit the ball
 * or miss tha ball. So there are at most 2 possible outcomes. This class generates a random number
 * and if that number is even then consider it as current player has hit the ball. If the generated
 * number is odd then consider it as player has miss the ball.
 */
class PlayerActionGenerator {
    fun generatePlayerEvent(): Int {
        return (Math.random() *100).toInt()
    }
}