package com.kotlin.tennisapplication.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kotlin.tennisapplication.Constant
import com.kotlin.tennisapplication.R
import com.kotlin.tennisapplication.databinding.ActivityMainBinding
import com.kotlin.tennisapplication.model.entity.PlayerEntity
import com.kotlin.tennisapplication.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PlayerViewModel.IPlayerAction {
    companion object {
        private const val TAG: String = "MainActivity"
    }

    private var isGameStarted: Boolean = false
    private lateinit var viewBinding: ActivityMainBinding
    private val uiHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewBinding.viewmodel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        viewBinding.lifecycleOwner = this

        winner_name.text = ""

        viewBinding.viewmodel?.setListener(this)
        viewBinding.viewmodel?.winner?.observe(this, Observer {
            toggleGame()
            winner_name.text = String.format(getString(R.string.player_wins), it.name)
        })
        viewBinding.viewmodel?.isDeuce?.observe(this, Observer {
            if (it) {
                setDeuceTexts()
            }
        })
        viewBinding.viewmodel?.hasAdvantage?.observe(this, Observer {
            if (it == viewBinding.viewmodel?.player1) {
                player_1.text = String.format(getString(R.string.player_display_score_advantage), 1)
            } else {
                player_2.text = String.format(getString(R.string.player_display_score_advantage), 2)
            }
        })
    }

    fun startGame(view: View) {
        toggleGame()
    }

    private fun toggleGame() {
        if (isGameStarted) {
            isGameStarted = false
            viewBinding.viewmodel?.stopGame()
            toggle_game.text = getString(R.string.start_new_game)
        } else {
            isGameStarted = true
            winner_name.text = ""
            toggle_game.text = getString(R.string.stop_game)
            player_1.text = String.format(getString(R.string.player_display_score), 1 , translateScore(0))
            player_2.text = String.format(getString(R.string.player_display_score), 2 , translateScore(0))
            uiHandler.postDelayed({
                viewBinding.viewmodel?.startGame()
            }, 1000)
        }
    }

    private fun setDeuceTexts(){
        player_1.text = String.format(getString(R.string.player_display_score), 1 , getString(R.string.deuce))
        player_2.text = String.format(getString(R.string.player_display_score), 2 , getString(R.string.deuce))
    }

    private fun translateScore(score: Int): String {
        when (score) {
            3 -> return "Forty"
            2 -> return "Thirty"
            1 -> return "Fifteen"
            0 -> return "Love"
        }
        throw IllegalArgumentException("Illegal score: $score")
    }

    class HideImageRunnable constructor(private val context: Context, private val view: ImageView) : Runnable {
        override fun run() {
            Glide.with(context)
                .clear(view)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiHandler.removeCallbacksAndMessages(null)
    }

    override fun onAction(action: Int, currentPlayer: PlayerEntity) {
        uiHandler.post {
            Log.d(TAG, "onAction " + currentPlayer.name + " totalPoints " + currentPlayer.totalPoints + " isOnAdvantage " + currentPlayer.isOnAdvantage)

            if (action == Constant.ACTION_GAINED_POINT) {
                Log.d(TAG, "player gained point " + currentPlayer.name)
                if (currentPlayer == viewBinding.viewmodel?.player1) {
                    player_1.text = String.format(getString(R.string.player_display_score), 1 , translateScore(
                        currentPlayer.totalPoints
                    ))
                } else if (currentPlayer == viewBinding.viewmodel?.player2) {
                    player_2.text = String.format(getString(R.string.player_display_score), 2, translateScore(
                        currentPlayer.totalPoints
                    ))
                }
            }
            if (action == Constant.ACTION_PLAYER_TOOK_TURN) {
                val playerView: ImageView = if (currentPlayer == viewBinding.viewmodel?.player1) {
                    img_player_1
                } else {
                    img_player_2
                }

                Glide.with(applicationContext)
                    .asGif()
                    .load(R.drawable.tennis_racket)
                    .into(playerView).waitForLayout()

                uiHandler.postDelayed({
                    HideImageRunnable(applicationContext, playerView).run()
                }, Constant.DELAY_PLAYER_IMAGE_HIDE)
            }
        }
    }
}