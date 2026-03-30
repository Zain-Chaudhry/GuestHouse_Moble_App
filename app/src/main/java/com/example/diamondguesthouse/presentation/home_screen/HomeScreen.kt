package com.example.diamondguesthouse.presentation.home_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.appNavigation.GuestHouseNavKey
import com.example.diamondguesthouse.appNavigation.NavCommand
import com.example.diamondguesthouse.appNavigation.OnGuestHouseNavigate
import com.example.diamondguesthouse.core.presentation.GenericTextView
import com.example.diamondguesthouse.theme.Zinc
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onNavigate: OnGuestHouseNavigate,
    viewModel: HomeScreenViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { ev ->
            when (ev) {
                is HomeUiEvent.Navigate -> onNavigate(NavCommand.Push(ev.key))
            }
        }
    }

    val monthlyIncome by viewModel.monthlyIncome.collectAsStateWithLifecycle()
    val checkIns by viewModel.todayCheckIns.collectAsStateWithLifecycle()
    val checkOuts by viewModel.todayCheckOuts.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, topBar, list, card) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 65.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            ) {
                Column {
                    GenericTextView(text = viewModel.greetingMessage(), style = MaterialTheme.typography.bodyLarge, color = Color.White)
                    GenericTextView(text = "Welcome to Diamond Guesthouse", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }

            HomeCardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                monthlyIncome = monthlyIncome,
                checkIns = checkIns,
                checkOuts = checkOuts,
                onNavigateTo = { key -> viewModel.submitUserEvent(HomeUserEvent.NavigateTo(key)) },
            )
            Column(
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(card.bottom, margin = 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            ) {
                Row {
                    HomeCardButtons(
                        title = "New Booking",
                        image = R.drawable.ic_add_icon,
                        modifier = Modifier.align(Alignment.Top),
                        onClick = { viewModel.submitUserEvent(HomeUserEvent.NavigateTo(GuestHouseNavKey.AddRecord)) },
                    )
                    HomeCardButtons(
                        title = "Search Record",
                        image = R.drawable.ic_search,
                        modifier = Modifier.align(Alignment.Top),
                        onClick = { viewModel.submitUserEvent(HomeUserEvent.NavigateTo(GuestHouseNavKey.SearchRecord)) },
                    )
                }
                Row {
                    HomeCardButtons(
                        title = "View Report",
                        image = R.drawable.ic_month,
                        modifier = Modifier.align(Alignment.Top),
                        onClick = { viewModel.submitUserEvent(HomeUserEvent.NavigateTo(GuestHouseNavKey.Report)) },
                    )
                    HomeCardButtons(
                        title = "View Booking",
                        image = R.drawable.ic_month,
                        modifier = Modifier.align(Alignment.Top),
                        onClick = { viewModel.submitUserEvent(HomeUserEvent.NavigateTo(GuestHouseNavKey.ViewBooking)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeCardItem(
    modifier: Modifier,
    monthlyIncome: String,
    checkIns: String,
    checkOuts: String,
    onNavigateTo: (GuestHouseNavKey) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                GenericTextView(text = "Total Revenue", fontSize = 16.sp, color = Color.White)
                GenericTextView(text = monthlyIncome, fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd).clickable {
                    onNavigateTo(GuestHouseNavKey.Settings)
                },
            )
        }
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column {
                Spacer(modifier = Modifier.size(20.dp))
                HomeCardRowItem(
                    image = R.drawable.ic_down_circle_arrow,
                    title = "Today's Check Ins",
                    rooms = checkIns,
                    onClick = { onNavigateTo(GuestHouseNavKey.ViewBooking) },
                )
                Spacer(modifier = Modifier.size(10.dp))
                HomeCardRowItem(
                    image = R.drawable.ic_up_circle_arrow,
                    title = "Today's Check Outs",
                    rooms = checkOuts,
                    onClick = { onNavigateTo(GuestHouseNavKey.ViewCheckOuts) },
                )
            }
        }
    }
}

@Composable
private fun HomeCardRowItem(image: Int, title: String, rooms: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(painter = painterResource(id = image), contentDescription = null)
        Spacer(modifier = Modifier.size(8.dp))
        GenericTextView(text = title, fontSize = 16.sp, color = Color.White)
        Spacer(modifier = Modifier.weight(1f))
        GenericTextView(text = rooms, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
private fun HomeCardButtons(title: String, image: Int, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .width(150.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.matchParentSize().align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                GenericTextView(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}
