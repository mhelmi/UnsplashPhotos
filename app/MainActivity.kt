package com.github.mhelmi.composeproj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.github.mhelmi.composeproj.ui.theme.ComposeProjTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

data class Moment(val title: String, val description: String, val backgroundColor: Color)

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ComposeProjTheme {
        MomentsScreen()
      }
    }
  }
}

@Composable
fun MomentsScreen() {
  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    val moments = listOf(
      Moment(
        "First Moment",
        "Description of the first moment",
        backgroundColor = randomColor()
      ),
      Moment(
        "Second Moment",
        "Description of the second moment",
        backgroundColor = randomColor()
      ),
      Moment(
        "Third Moment",
        "Description of the third moment",
        backgroundColor = randomColor()
      ),
      Moment(
        "Forth Moment",
        "Description of the forth moment",
        backgroundColor = randomColor()
      ),
      Moment(
        "Fifth Moment",
        "Description of the fifth moment",
        backgroundColor = randomColor()
      ),
    )
    ReversedMomentsList(moments, modifier = Modifier.padding(innerPadding))
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReversedMomentsList(
  moments: List<Moment>,
  modifier: Modifier = Modifier,
) {

  var momentList by remember { mutableStateOf(moments) }

  Box(
    modifier = modifier.fillMaxSize()
  ) {
    momentList.forEachIndexed { index, moment ->
      var offsetY by remember { mutableFloatStateOf(0f) }

      val reversedIndex = momentList.size - index
      // Calculate overlap percentage (5% for each subsequent item)
      val overlap = 0.2f * reversedIndex
      val width = 300.dp
      val widthReduction = 30.dp
      val calculatedWidth = width - (widthReduction * index)

      val height = 300.dp
      val heightReduction = 30.dp
      val calculatedHeight = height - (heightReduction * index)

      Box(
        modifier = Modifier
          .width(calculatedWidth)
          .height(calculatedHeight)
          .zIndex(-index.toFloat()) // Higher index means lower in the stack
          .offset(y = (overlap * 100 + offsetY).dp) // Use offsetY to move item
          .pointerInput(Unit) {
            detectVerticalDragGestures(
              onVerticalDrag = { change, dragAmount ->
                change.consume() // Mark the event as handled
                offsetY += dragAmount // Move the card by drag amount
                if (offsetY <= 0) {
                  offsetY = 0f
                }
              },
              onDragEnd = {
                // If dragged down sufficiently, dismiss the item
                if (offsetY > 270f) { // Dismiss threshold
                  momentList = momentList
                    .toMutableList()
                    .apply {
                      removeAt(index)
                      offsetY = 0f
                    }
                } else {
                  // Reset the position if not enough drag
                  offsetY = 0f
                }
              }
            )
          }
      ) {
        MomentItem(
          moment, modifier = Modifier
            .width(calculatedWidth)
            .height(calculatedHeight)
        )
      }
    }
  }
}

@Composable
fun MomentItem(moment: Moment, modifier: Modifier = Modifier) {
  Card(
    modifier = modifier
      .padding(16.dp),
    colors = CardDefaults.cardColors(containerColor = moment.backgroundColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(text = moment.title, style = MaterialTheme.typography.headlineLarge)
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = moment.description, style = MaterialTheme.typography.bodyMedium)
    }
  }
}

fun randomColor(): Color {
  val random = Random
  return Color(
    red = random.nextFloat(),
    green = random.nextFloat(),
    blue = random.nextFloat(),
    alpha = 1f
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ComposeProjTheme {
    MomentsScreen()
  }
}