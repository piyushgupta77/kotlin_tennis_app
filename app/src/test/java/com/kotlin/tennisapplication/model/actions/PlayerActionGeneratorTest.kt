package com.kotlin.tennisapplication.model.actions

import org.junit.Assert.assertTrue
import org.junit.Test

class PlayerActionGeneratorTest {

    @Test
    fun generatePlayerEvent() {
        val playerActionGenerator = PlayerActionGenerator()
        val event:Int = playerActionGenerator.generatePlayerEvent()
        assertTrue(event > 0)
    }
}