package no.nordicsemi.android.material.you

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tutorial(items: List<@Composable () -> Unit>, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState()
        val currentPage = pagerState.currentPage
        val scope = rememberCoroutineScope()

        HorizontalPager(
            count = items.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { i ->
            items[i].invoke()
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
        )
        if (currentPage == items.lastIndex) {
            Button(onClick = { onFinish() }) {
                Text("Finish")
            }
        } else {
            Button(onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage+1) }}) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.size(16.dp))
    }
}
