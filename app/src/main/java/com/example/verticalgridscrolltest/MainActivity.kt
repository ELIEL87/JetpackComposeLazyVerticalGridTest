package com.example.verticalgridscrolltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.verticalgridscrolltest.ui.theme.VerticalGridScrollTestTheme
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VerticalGridScrollTestTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val gridItems = (1..50).map { GridItem(it.toString()) }

                    LazyVerticalGrid(items = gridItems, gridItemContent = gridItemContent)
                }
            }
        }
    }
}

data class GridItem(val description: String)

val gridItemContent: @Composable (BoxScope.(GridItem) -> Unit) = { (description) ->
    Box {
        Card(
            backgroundColor = Color.Blue,
            modifier = Modifier
                .padding(4.dp)
                .height(192.dp)
                .fillMaxWidth(),
            elevation = 8.dp,
        ) {
            Text(
                text = description,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun <T> LazyVerticalGrid(
    items: List<T>,
    gridItemContent: @Composable (BoxScope.(T) -> Unit),
) {
    val listState = rememberLazyListState()
    var viewportHeight by remember { mutableStateOf(0) }

    LazyVerticalGrid(
        cells = GridCells.Adaptive(256.dp),
        state = listState,
        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .onGloballyPositioned { viewportHeight = it.size.height }
            .testTag("browsable-media-container"),
        content = {
            itemsIndexed(items) { index, item ->
                val height by remember { mutableStateOf(192) }
                val column = index % 5
                val row = (index - column) / 5
                val scrollEffect: suspend CoroutineScope.() -> Unit = {
                    val targetRow = if (row > 0) row - 1 else row
                    listState.animateScrollToItem(
                        targetRow,
                        if (row > 0) (height - (viewportHeight / 2 - height / 2)).coerceAtLeast(0) else 0
                    )
                }
                MediaItemContainer(onFocused = scrollEffect) {
                    gridItemContent(item)
                }
            }
        }
    )
}

val roundedCornerShape = RoundedCornerShape(16.dp)

@Composable
private fun MediaItemContainer(
    onFocused: suspend CoroutineScope.() -> Unit,
    contents: @Composable (BoxScope.() -> Unit),
) {
    Surface(
        modifier = Modifier.fillMaxHeight()
    ) {
        var isFocused by remember { mutableStateOf(false) }
        LaunchedEffect(isFocused) { if (isFocused) onFocused() }
        Box(
            modifier = Modifier
                .border(
                    4.dp,
                    if (isFocused) Color.Black else Color.Transparent,
                    shape = roundedCornerShape
                )
                .onFocusChanged { focusState ->
                    val focused = focusState.isFocused
                    isFocused = focused
                }
                .focusable(true)
        ) {
            Box(Modifier.padding(24.dp)) {
                contents()
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview(widthDp = 1536 - 80 - 80, showBackground = true, backgroundColor = 0xFFA0A0A0)
@Composable
fun DefaultPreview() {
    VerticalGridScrollTestTheme {
        Surface(color = MaterialTheme.colors.background) {
            val gridItems = (1..50).map { GridItem(it.toString()) }

            LazyVerticalGrid(
                items = gridItems,
                gridItemContent = gridItemContent
            )
        }
    }
}
