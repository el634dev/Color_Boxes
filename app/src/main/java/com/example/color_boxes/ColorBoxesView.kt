package com.example.color_boxes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class ColorBoxesView: ViewModel(){
    var job: Job? = null
    val count by mutableIntStateOf(0)
    //    var currentColor by remember mutableStateOf(Color.Red)

    var activeIndex by mutableIntStateOf(-1)
    var isRunning by mutableStateOf(false)
    var isRandom by mutableStateOf(false)

    // Memory Game Logic
    var sequence by mutableStateOf(listOf<Int>())
    var userSequence by mutableStateOf(listOf<Int>())
    var isShowing by mutableStateOf(false)
    var message by mutableStateOf("Start")
}
