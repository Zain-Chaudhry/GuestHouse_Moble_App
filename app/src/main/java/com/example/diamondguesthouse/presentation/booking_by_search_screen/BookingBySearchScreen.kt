package com.example.diamondguesthouse.presentation.booking_by_search_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.appNavigation.GuestHouseNavKey
import com.example.diamondguesthouse.appNavigation.NavCommand
import com.example.diamondguesthouse.appNavigation.OnGuestHouseNavigate
import com.example.diamondguesthouse.core.presentation.CustomDateField
import com.example.diamondguesthouse.core.presentation.CustomTextField
import com.example.diamondguesthouse.core.presentation.TimeField
import com.example.diamondguesthouse.presentation.add_record_screen.component.AddRecordDropdown
import com.example.diamondguesthouse.presentation.search_booking.SearchBookingScreenViewModel
import com.example.diamondguesthouse.presentation.search_booking.SearchBookingUiEvent
import com.example.diamondguesthouse.presentation.search_booking.SearchBookingUserEvent
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun BookingBySearchScreen(
    onNavigate: OnGuestHouseNavigate,
    sharedViewModel: SearchBookingScreenViewModel = koinActivityViewModel(),
) {
    val context = LocalContext.current
    val uiState by sharedViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        sharedViewModel.uiEvents.collect { ev ->
            when (ev) {
                SearchBookingUiEvent.NavigateHome -> onNavigate(NavCommand.ReplaceRoot(GuestHouseNavKey.Home))
                is SearchBookingUiEvent.Message -> Toast.makeText(context, ev.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBar, nameRow, surface) = createRefs()
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrorw_back),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { onNavigate(NavCommand.Pop) },
                )
                Text(
                    text = "New Booking",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .constrainAs(surface) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    item {
                        SearchBookingRoomForm(sharedViewModel, uiState)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Customers:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.size(10.dp))
                    }
                    if (uiState.selectedCustomers.isEmpty()) {
                        item {
                            Text(text = "No customers selected", modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        items(uiState.selectedCustomers) { customer ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                Text(
                                    text = "${uiState.selectedCustomers.indexOf(customer) + 1}. ${customer.name}",
                                )
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                sharedViewModel.submitUserEvent(SearchBookingUserEvent.ConfirmBookingClicked)
                            },
                        ) {
                            Text(text = "Confirm Booking")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBookingRoomForm(
    viewModel: SearchBookingScreenViewModel,
    uiState: com.example.diamondguesthouse.presentation.search_booking.SearchBookingUiState,
) {
    val roomNum = listOf("110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120")
    AddRecordDropdown(
        value = uiState.roomNo,
        label = "Room No.",
        list = roomNum,
        onSelectedChange = { viewModel.submitUserEvent(SearchBookingUserEvent.RoomNoChanged(it)) },
        isAvailable = true,
    )
    CustomDateField(
        label = "Check Out Date",
        value = uiState.checkOutDate,
        onDateSelected = { viewModel.submitUserEvent(SearchBookingUserEvent.CheckOutDateChanged(it)) },
        isClickable = true,
    )
    TimeField(
        label = "Check Out Time",
        value = uiState.checkOutTime,
        onTimeSelected = { viewModel.submitUserEvent(SearchBookingUserEvent.CheckOutTimeChanged(it)) },
        isClickable = true,
    )
    CustomTextField(
        label = "Amount Received",
        value = uiState.roomPrice,
        onValueChange = { viewModel.submitUserEvent(SearchBookingUserEvent.RoomPriceChanged(it)) },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
