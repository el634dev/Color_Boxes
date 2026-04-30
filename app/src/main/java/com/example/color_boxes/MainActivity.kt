package com.example.color_boxes

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.color_boxes.ui.theme.Color_BoxesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Color_BoxesTheme {
                ColorBoxScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorBoxScreen(modifier: Modifier = Modifier, colorBoxesView: ColorBoxesView = viewModel()) {
    val coroutine = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Colors!") }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(color = Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(

            ) {
                for(row in 0..1){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        for(col in 0..3){
                            val index = row * 2 + col
                            ColorBox(
                                color = colorBoxesView.colorList[index],
                                isActive = colorBoxesView.activeIndex == index,
                                onClick = {
                                    if(!colorBoxesView.isRunning && colorBoxesView.sequence.isNotEmpty()){
                                        handleUserClick(index, colorBoxesView)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                ColorBox(
                    color = Color.Red,
                    isActive = colorBoxesView.activeIndex == 0,
                    onClick = {
                        if (colorBoxesView.isRunning) {
                            handleUserClick(0, colorBoxesView)
                        }
                    }
                )
                ColorBox(
                    color = Color.Yellow,
                    isActive = colorBoxesView.activeIndex == 1,
                    onClick = {
                        if (colorBoxesView.isRunning) {
                            handleUserClick(1, colorBoxesView)
                        }
                    }
                )
            }
            // ----------------------------------------
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                ColorBox(
                    color = Color.Green,
                    isActive = colorBoxesView.activeIndex == 3,
                    onClick = {
                        if (colorBoxesView.isRunning) {
                            handleUserClick(3, colorBoxesView)
                        }
                    }
                )
                ColorBox(color = Color.Blue,
                    isActive = colorBoxesView.activeIndex == 2,
                    onClick = {
                        if (colorBoxesView.isRunning) {
                            handleUserClick(2, colorBoxesView)
                        }
                    }
                )
            }
            Button (
                modifier = Modifier.padding(top = 20.dp),
                onClick = {
                    if (colorBoxesView.isRunning) {
                        colorBoxesView.job?.cancel()
                        colorBoxesView.activeIndex = -1
                        colorBoxesView.isRunning = false
                    } else {
                        colorBoxesView.isRunning = true
                        colorBoxesView.job = coroutine.launch {
                            var current = 0
                            while (true) {
                                colorBoxesView.activeIndex = current
                                delay(500)
                                colorBoxesView.activeIndex = -1
                                delay(200)

                                // 2. Toggle logic based on checkbox
                                if (colorBoxesView.isRandom) {
                                    current = (0..3).random()
                                } else {
                                    current = (current + 1) % 4
                                }
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = if (colorBoxesView.isRunning) "Stop" else "Start",
                    fontSize = 15.sp
                )
            }
            // Start Game
            Button(
                onClick = {
                    startGame(coroutine, colorBoxesView)
                }
            ){
                Text(
                    text = colorBoxesView.message,
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
            // --------------------------------------
            CheckBox(label = "Randomize?", colorBoxesView.isRandom, onToggle = { colorBoxesView.isRandom = it })
        }
    }
}

// ---------------------------------
// COLOR BOX FUNCTION
@Composable
fun ColorBox(color: Color, isActive: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(150.dp, 200.dp)
            .background(if (isActive) Color.White else color)
            .clickable{ onClick() }
    )
}

// ---------------------------------
// CHECKBOX FUNCTION
@Composable
fun CheckBox(label: String, isChecked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 16.dp)
            .toggleable(
                value = isChecked,
                onValueChange = { onToggle(it) },
                role = Role.Checkbox
            )
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onToggle(it) }
        )
        Text(
            text = label,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

// ---------------------------------
// NEXT ROUND FUNCTION  (starts a new round if the player wants to play again)
fun nextRound(colorBoxesView: ColorBoxesView, coroutineScope: CoroutineScope){
    colorBoxesView.userSequence = emptyList()
    colorBoxesView.userSequence += colorBoxesView.sequence + (0..3).random()
    colorBoxesView.isRunning = true

    colorBoxesView.message = "Waiting.."
    colorBoxesView.job = coroutineScope.launch {
        delay(1000)
        for(index in colorBoxesView.sequence){
            colorBoxesView.activeIndex = index
            delay(600)
            colorBoxesView.activeIndex = -1

            delay(200)
        }
        colorBoxesView.isRunning = false
        colorBoxesView.message = "Your turn"
    }
}

// ---------------------------------
// START GAME FUNCTION
fun startGame(coroutineScope: CoroutineScope, colorBoxesView: ColorBoxesView){
    colorBoxesView.userSequence = emptyList()
    nextRound(colorBoxesView, coroutineScope)
}

// ---------------------------------
fun handleUserClick(index: Int, colorBoxesView: ColorBoxesView){
    val currentStep = colorBoxesView.userSequence.size
    if(index == colorBoxesView.sequence[currentStep]){
        colorBoxesView.userSequence += index
        colorBoxesView.message = "Correct"
    } else {
        colorBoxesView.message = "Try again"
        colorBoxesView.sequence = emptyList()
    }
}
