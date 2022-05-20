package no.nordicsemi.android.material.you

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class PagerViewEntity(
    val items: List<PagerViewItem>,
)

class PagerViewItem(
    val title: String,
    val drawView: @Composable () -> Unit,
)

@Suppress("unused")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerView(viewEntity: PagerViewEntity) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState()
        val tabIndex = pagerState.currentPage
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = tabIndex,
            containerColor = colorResource(id = R.color.appBarColor),
            contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = @Composable { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
        ) {
            viewEntity.items.forEachIndexed { index, item ->
                Tab(
                    selected = tabIndex == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = item.title)
                    }
                )
            }
        }

        HorizontalPager(
            count = viewEntity.items.size,
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { i ->
            viewEntity.items[i].drawView()
        }
    }
}
