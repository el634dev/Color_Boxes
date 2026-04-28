package com.example.color_boxes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
fun ColorBoxScreen(modifier: Modifier = Modifier) {
    val coroutine = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }
    val count = remember { mutableIntStateOf(0) }

    var activeIndex by remember {mutableIntStateOf(-1)}
    var isRunning by remember { mutableStateOf(false) }

//    var currentColor by remember { mutableStateOf(Color.Red) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Colors!") }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(color = Color.Black).fillMaxSize().padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                ColorBox(color = Color.Red, isActive = activeIndex == 0)
                ColorBox(color = Color.Yellow, isActive = activeIndex == 1)
            }
            // ----------------------------------------
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                ColorBox(color = Color.Green, isActive = activeIndex == 2)
                ColorBox(color = Color.Blue, isActive = activeIndex == 3)
            }
            Button (
                modifier = Modifier.padding(top = 20.dp),
                onClick = {
                    if (isRunning) {
                        job?.cancel()
                        activeIndex = -1 // Reset
                        isRunning = false
                    } else {
                        isRunning = true
                        job = coroutine.launch {
                            var current = 0
                            while (true) {
                                activeIndex = current
                                delay(500)

                                activeIndex = -1
                                delay(200)
                                current = (current + 1) % 4
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = if (isRunning) "Stop" else "Start",
                    fontSize = 15.sp
                )
            }
//            Need a checkbox that randomizes the color change
            CheckBox(label = "Randomize?")
        }
    }
}

// ---------------------------------
// COLOR BOX FUNCTION
@Composable
fun ColorBox(color: Color, isActive: Boolean) {
    Box(
        modifier = Modifier.size(150.dp, 200.dp).background(if(isActive) Color.White else color)
    )
}

// ---------------------------------
@Composable
fun CheckBox(label: String){
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.toggleable(
                value = checked, onValueChange = { checked = it }, role = Role.Checkbox)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )
        Text(
            "Randomize?",
            color = Color.White,
            fontSize = 18.sp
        )
    }
    Text(
        if (checked) "Randomize?" else "Randomize?"
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Color_BoxesTheme {
        ColorBoxScreen()
    }
}