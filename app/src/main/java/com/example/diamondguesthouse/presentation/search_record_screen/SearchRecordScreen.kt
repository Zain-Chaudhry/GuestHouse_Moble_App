package com.example.diamondguesthouse.presentation.search_record_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.diamondguesthouse.presentation.search_booking.SearchBookingScreenViewModel
import com.example.diamondguesthouse.presentation.search_booking.SearchBookingUserEvent
import org.koin.compose.viewmodel.koinActivityViewModel

@Composable
fun SearchRecordScreen(
    onNavigate: OnGuestHouseNavigate,
    sharedViewModel: SearchBookingScreenViewModel = koinActivityViewModel(),
) {
    val context = LocalContext.current
    val selectedTab = rememberSaveable { mutableIntStateOf(0) }
    val uiState by sharedViewModel.uiState.collectAsStateWithLifecycle()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    },
                horizontalArrangement = Arrangement.spacedBy(80.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrorw_back),
                    contentDescription = null,
                    modifier = Modifier.clickable { onNavigate(NavCommand.Pop) },
                )
                Text(
                    text = "Search Customer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically),
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
                Column {
                    TabRow(selectedTabIndex = selectedTab.intValue) {
                        Tab(
                            selected = selectedTab.intValue == 0,
                            onClick = { selectedTab.intValue = 0 },
                            text = { Text("Local") },
                        )
                        Tab(
                            selected = selectedTab.intValue == 1,
                            onClick = { selectedTab.intValue = 1 },
                            text = { Text("Foreigner") },
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        when (selectedTab.intValue) {
                            0 -> {
                                OutlinedTextField(
                                    label = { Text("Enter CNIC") },
                                    value = uiState.cnic,
                                    onValueChange = {
                                        sharedViewModel.submitUserEvent(SearchBookingUserEvent.CnicChanged(it))
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done,
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))
                                Button(onClick = { sharedViewModel.submitUserEvent(SearchBookingUserEvent.SearchClicked) }) {
                                    Text(text = "Search")
                                }
                                if (uiState.searchError.isNotEmpty()) {
                                    Text(text = uiState.searchError, color = Color.Red)
                                } else if (uiState.searchResults.isNotEmpty()) {
                                    Text(text = "Customers Found:", fontWeight = FontWeight.Bold)
                                    LazyColumn {
                                        items(uiState.searchResults) { customer ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                            ) {
                                                Checkbox(
                                                    checked = uiState.selectedCustomers.contains(customer),
                                                    onCheckedChange = { isChecked ->
                                                        if (isChecked) {
                                                            sharedViewModel.submitUserEvent(
                                                                SearchBookingUserEvent.AddCustomer(customer),
                                                            )
                                                            Toast.makeText(
                                                                context,
                                                                "Customer ${customer.name} Added",
                                                                Toast.LENGTH_SHORT,
                                                            ).show()
                                                        } else {
                                                            sharedViewModel.submitUserEvent(
                                                                SearchBookingUserEvent.RemoveCustomer(customer),
                                                            )
                                                        }
                                                        Log.d("SelectedCustomers", "Selected: ${uiState.selectedCustomers}")
                                                    },
                                                )
                                                Text(text = "${customer.name} (${customer.cnic})")
                                            }
                                        }
                                    }
                                    if (uiState.selectedCustomers.isNotEmpty()) {
                                        Button(onClick = {
                                            onNavigate(NavCommand.Push(GuestHouseNavKey.BookingBySearch))
                                        }) {
                                            Text("Proceed to booking")
                                        }
                                    } else {
                                        Text("No customers selected")
                                    }
                                }
                            }
                            1 -> {
                                OutlinedTextField(
                                    label = { Text("Enter Passport No") },
                                    value = uiState.passportNo,
                                    onValueChange = {
                                        sharedViewModel.submitUserEvent(SearchBookingUserEvent.PassportChanged(it))
                                    },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done,
                                    ),
                                )
                                Button(onClick = { sharedViewModel.submitUserEvent(SearchBookingUserEvent.SearchClicked) }) {
                                    Text(text = "Search")
                                }
                                if (uiState.searchError.isNotEmpty()) {
                                    Text(text = uiState.searchError, color = Color.Red)
                                } else if (uiState.searchResults.isNotEmpty()) {
                                    Text(text = "Customers Found:", fontWeight = FontWeight.Bold)
                                    LazyColumn {
                                        items(uiState.searchResults) { customer ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                            ) {
                                                Checkbox(
                                                    checked = uiState.selectedCustomers.contains(customer),
                                                    onCheckedChange = { isChecked ->
                                                        if (isChecked) {
                                                            sharedViewModel.submitUserEvent(
                                                                SearchBookingUserEvent.AddCustomer(customer),
                                                            )
                                                            Toast.makeText(
                                                                context,
                                                                "Customer ${customer.name} Added ",
                                                                Toast.LENGTH_SHORT,
                                                            ).show()
                                                        } else {
                                                            sharedViewModel.submitUserEvent(
                                                                SearchBookingUserEvent.RemoveCustomer(customer),
                                                            )
                                                        }
                                                    },
                                                )
                                                Text(text = "${customer.name} (${customer.passportNo})")
                                            }
                                        }
                                    }
                                    if (uiState.selectedCustomers.isNotEmpty()) {
                                        Button(onClick = {
                                            onNavigate(NavCommand.Push(GuestHouseNavKey.BookingBySearch))
                                        }) {
                                            Text("Proceed to booking")
                                        }
                                    } else {
                                        Text("No customers selected")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
