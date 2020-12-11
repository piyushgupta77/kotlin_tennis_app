package com.kotlin.tennisapplication.model.points

import androidx.lifecycle.MutableLiveData
import com.kotlin.tennisapplication.Constant
import com.kotlin.tennisapplication.model.entity.PlayerEntity
import com.kotlin.tennisapplication.viewmodel.PlayerViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PointsProcessorTest {

    @Mock
    lateinit var player1: PlayerEntity

    @Mock
    lateinit var player2: PlayerEntity

    private lateinit var pointsProcessor: PointsProcessor

    @Mock
    lateinit var viewModel: PlayerViewModel

    @Mock
    lateinit var hasAdvantage: MutableLiveData<PlayerEntity>

    @Mock
    lateinit var winner: MutableLiveData<PlayerEntity>

    @Mock
    lateinit var isDeuce: MutableLiveData<Boolean>

    @Mock
    lateinit var playerEntity: PlayerEntity

    @Mock
    lateinit var playerAction: PlayerViewModel.IPlayerAction

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        pointsProcessor = PointsProcessor()
    }

    @Test
    fun testPlayer1ScoreIncrement() {
        Mockito.`when`(player1.totalPoints).thenReturn(1)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(1)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(playerAction).onAction(Constant.ACTION_GAINED_POINT, player1)
    }

    @Test
    fun testAdvantageGivenPlayer1() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)

        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(hasAdvantage).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player1, playerEntityCaptor.value)
    }

    @Test
    fun testAdvantageGivenPlayer1AndNoDeuce() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)

        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(hasAdvantage).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player1, playerEntityCaptor.value)
        //verify nothing is posted for isDeuce LiveData
        Mockito.verifyNoMoreInteractions(isDeuce)
    }

    @Test
    fun testOnlyAdvantageGivenPlayer1AndNoDeuceNoWinner() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)
        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(hasAdvantage).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player1, playerEntityCaptor.value)
        //verify nothing is posted for isDeuce LiveData
        Mockito.verifyNoMoreInteractions(isDeuce)
        Mockito.verifyNoMoreInteractions(winner)
    }

    @Test
    fun testDeuceHappenedWithoutAdvantagePoint() {
        Mockito.`when`(player1.totalPoints).thenReturn(2)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)

        Mockito.`when`(isDeuce.value).thenReturn(true)
        Mockito.`when`(viewModel.isDeuce).thenReturn(isDeuce)


        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(isDeuce).postValue(true)
    }

    @Test
    fun testDeuceHappenedWithAdvantagePoint() {
        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(true)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)

        Mockito.`when`(isDeuce.value).thenReturn(true)
        Mockito.`when`(viewModel.isDeuce).thenReturn(isDeuce)


        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(isDeuce).postValue(true)
    }

    @Test
    fun testWinnerPlayer1() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player2.totalPoints).thenReturn(2)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)
        Mockito.`when`(viewModel.winner).thenReturn(winner)
        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(winner).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player1, playerEntityCaptor.value)
    }

    @Test
    fun testWinnerPlayer1WithAdvantage() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(3)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(true)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)
        Mockito.`when`(viewModel.winner).thenReturn(winner)

        pointsProcessor.processScore(playerAction, viewModel, player1, player2)

        Mockito.verify(winner).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player1, playerEntityCaptor.value)
    }

    @Test
    fun testWinnerPlayer2() {
        val playerEntityCaptor: ArgumentCaptor<PlayerEntity> =
            ArgumentCaptor.forClass(PlayerEntity::class.java)

        Mockito.`when`(player1.totalPoints).thenReturn(1)
        Mockito.`when`(player1.isOnAdvantage).thenReturn(false)
        Mockito.`when`(player2.totalPoints).thenReturn(3)
        Mockito.`when`(player2.isOnAdvantage).thenReturn(false)

        Mockito.`when`(hasAdvantage.value).thenReturn(playerEntity)
        Mockito.`when`(viewModel.hasAdvantage).thenReturn(hasAdvantage)
        Mockito.`when`(viewModel.winner).thenReturn(winner)

        // here player 2 is set as current player
        pointsProcessor.processScore(playerAction, viewModel, player2, player1)

        Mockito.verify(winner).postValue(playerEntityCaptor.capture())
        Assert.assertEquals(player2, playerEntityCaptor.value)
    }
}