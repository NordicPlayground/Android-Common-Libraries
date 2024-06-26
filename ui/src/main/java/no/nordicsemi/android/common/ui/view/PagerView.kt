/*
 * Copyright (c) 2023, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.common.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.nordicsemi.android.common.ui.R

class PagerViewEntity(
    val items: List<PagerViewItem>,
)

class PagerViewItem(
    val title: String,
    val drawView: @Composable () -> Unit,
)

@Composable
fun PagerView(
    viewEntity: PagerViewEntity,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 0.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
) {
    PagerView(
        viewEntity = viewEntity,
        modifier = modifier,
        itemSpacing = itemSpacing,
        contentPadding = contentPadding,
        verticalAlignment = verticalAlignment,
        coroutineScope = rememberCoroutineScope()
    )
}

@Composable
fun PagerView(
    viewEntity: PagerViewEntity,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 0.dp,
    scrollable: Boolean = true,
    coroutineScope: CoroutineScope,
    pagerState: PagerState = rememberPagerState(pageCount = { viewEntity.items.size }),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
) {
    Column(
        modifier = modifier,
    ) {
        val tabIndex = pagerState.currentPage

        val edgePadding = maxOf(
            contentPadding.calculateLeftPadding(LayoutDirection.Ltr),
            contentPadding.calculateRightPadding(LayoutDirection.Rtl)
        )
        if (scrollable) {
            ScrollableTabRow(
                edgePadding = edgePadding,
                selectedTabIndex = tabIndex,
                containerColor = colorResource(id = R.color.appBarColor),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                indicator = @Composable { tabPositions ->
                    SecondaryIndicator(
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
        } else {
            // This box adds a padding on the sides to accommodate the cutout (edge padding).
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.appBarColor))
                    .padding(horizontal = edgePadding)
            ) {
                TabRow(
                    modifier = Modifier.fillMaxWidth(),
                    selectedTabIndex = tabIndex,
                    containerColor = colorResource(id = R.color.appBarColor),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    indicator = @Composable { tabPositions ->
                        SecondaryIndicator(
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
            }
        }

        HorizontalPager(
            state = pagerState,
            contentPadding = contentPadding,
            pageSpacing = itemSpacing,
            verticalAlignment = verticalAlignment,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { i ->
            viewEntity.items[i].drawView()
        }
    }
}
