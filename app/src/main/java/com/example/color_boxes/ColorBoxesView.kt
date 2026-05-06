package com.example.color_boxes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class ColorBoxesView: ViewModel(){
    var job: Job? = null
    val count by mutableIntStateOf(0)
    val colorList = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow)

    var activeIndex = mutableIntStateOf(-1)
    var isRunning by mutableStateOf(false)
    var isRandom by mutableStateOf(false)

    // Memory Game Logic
    var sequence = mutableStateListOf<Int>()
    var userSequence = mutableStateListOf<Int>()
    var isShowing by mutableStateOf(false)
    var message by mutableStateOf("Start")
}
