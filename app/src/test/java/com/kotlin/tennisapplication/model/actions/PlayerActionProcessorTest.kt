package com.kotlin.tennisapplication.model.actions

import com.kotlin.tennisapplication.Constant
import com.kotlin.tennisapplication.model.entity.PlayerEntity
import com.kotlin.tennisapplication.model.points.PointsProcessor
import com.kotlin.tennisapplication.viewmodel.PlayerViewModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class PlayerActionProcessorTest {

    private lateinit var playerActionProcessor: PlayerActionProcessor

    @Mock
    lateinit var player1: PlayerEntity

    @Mock
    lateinit var player2: PlayerEntity

    @Mock
    lateinit var pointsProcessor: PointsProcessor

    @Mock
    lateinit var viewModel: PlayerViewModel

    @Mock
    lateinit var runnable: Runnable

    @Mock
    lateinit var playerAction: PlayerViewModel.IPlayerAction

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        playerActionProcessor = PlayerActionProcessor(pointsProcessor)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSamePlayersProcessed() {
        playerActionProcessor.processActionEvent(
            playerAction,
            viewModel,
            Constant.ACTION_HIT,
            player1,
            player1
        )
    }

    @Test
    fun testDifferentPlayersNoError() {
        playerActionProcessor.processActionEvent(
            playerAction,
            viewModel,
            Constant.ACTION_HIT,
            player1,
            player2
        )
    }

    @Test
    fun testPlayer1HitsBall() {
        player1 = PlayerEntity(
            0,
            runnable,
            "Player 1"
        )
        player2 = PlayerEntity(
            0,
            runnable,
            "Player 2"
        )
        val initialHit = player1.hitCount
        playerActionProcessor.processActionEvent(
            playerAction,
            viewModel,
            Constant.ACTION_HIT,
            player1,
            player2
        )
        assertTrue(player1.hitCount == initialHit + 1)
    }

    @Test
    fun testPlayer1MissesBall() {
        player1 = PlayerEntity(
            0,
            runnable,
            "Player 1"
        )
        player2 = PlayerEntity(
            0,
            runnable,
            "Player 2"
        )
        val initialHit = player1.missCount

        playerActionProcessor.processActionEvent(
            playerAction,
            viewModel,
            Constant.ACTION_MISS,
            player1,
            player2
        )

        assertTrue(player1.missCount == initialHit + 1)
        verify(pointsProcessor).processScore(playerAction, viewModel, player2, player1)
    }

    @Test
    fun testPlayer1MissPlayer2GainPoint() {
        player1 = PlayerEntity(
            0,
            runnable,
            "Player 1"
        )
        player2 = PlayerEntity(
            1,
            runnable,
            "Player 2"
        )
        val initialHit = player1.missCount

        playerActionProcessor.processActionEvent(
            playerAction,
            viewModel,
            Constant.ACTION_MISS,
            player1,
            player2
        )

        assertTrue(player1.missCount == initialHit + 1)
        verify(pointsProcessor).processScore(playerAction, viewModel, player2, player1)
    }
}