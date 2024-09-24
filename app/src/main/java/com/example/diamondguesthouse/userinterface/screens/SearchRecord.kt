package com.example.diamondguesthouse.userinterface.screens

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.viewmodel.SearchRecordViewModel


@Composable
fun SearchRecord(navController: NavController, sharedViewModel: SearchRecordViewModel) {
    val context = LocalContext.current
    val selectedTab = rememberSaveable { mutableIntStateOf(0) }


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
                }
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrorw_back),
                    contentDescription = null,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = "Search Customer",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
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
                    }
            ) {
                Column {

                    TabRow(
                        selectedTabIndex = selectedTab.intValue
                    ) {
                        Tab(
                            selected = selectedTab.intValue == 0,
                            onClick = { selectedTab.intValue = 0 },
                            text = { Text("Local") }
                        )
                        Tab(
                            selected = selectedTab.intValue == 1,
                            onClick = { selectedTab.intValue = 1 },
                            text = { Text("Foreigner") }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                            // Search Field based on Selected Tab
                            when (selectedTab.intValue) {
                                0 -> {
                                    OutlinedTextField(
                                        label = { Text("Enter CNIC") },
                                        value = sharedViewModel.cnic,
                                        onValueChange = { sharedViewModel.cnic = it },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Done
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))

                                    Button(onClick = {
                                      sharedViewModel.searchCustomer(GuestHouseDatabase.getDatabase(context).customerDao())
                                    }) {
                                        Text(text = "Search")
                                    }

                                    if (sharedViewModel.errorMessage.isNotEmpty()) {
                                        Text(text = sharedViewModel.errorMessage, color = Color.Red)
                                    } else {
                                        if (sharedViewModel.searchResults.isNotEmpty()) {

                                            Text(text = "Customers Found:", fontWeight = FontWeight.Bold)
                                            LazyColumn {
                                                items(sharedViewModel.searchResults) { customer ->
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(8.dp)
                                                    ) {
                                                        Checkbox(
                                                            checked = sharedViewModel.selectedCustomers.contains(customer),
                                                            onCheckedChange = { isChecked ->
                                                                if (isChecked) {
                                                                    sharedViewModel.addCustomer(customer)
                                                                    Toast.makeText(context,"Customer ${customer.name} Added",Toast.LENGTH_SHORT).show()}
                                                                else {
                                                                    sharedViewModel.removeCustomer(customer)
                                                                    Toast.makeText(context,"Customer ${customer.name} Removed",Toast.LENGTH_SHORT).show()
                                                                }
                                                                // Log the current selected customers for debugging
                                                                Log.d("SelectedCustomers", "Current selected customers: ${sharedViewModel.selectedCustomers}")
                                                            }
                                                        )
                                                        Text(text = "${customer.name} (${customer.cnic})")
                                                    }
                                                }
                                            }
                                            if (sharedViewModel.selectedCustomers.isNotEmpty()) {
                                                Button(onClick = {
                                                    Log.d("ProceedToBooking", "Selected customers: ${sharedViewModel.selectedCustomers}")
                                                    navController.navigate("booking_by_search")
                                                }) {
                                                    Text("Proceed to booking")
                                                }
                                            } else {
                                                Text("No customers selected")
                                            }
                                        }
                                    }
                                }
                                1 -> {
                                    OutlinedTextField(
                                        label = { Text("Enter Passport No") },
                                        value = sharedViewModel.passportNo,
                                        onValueChange = { sharedViewModel.passportNo = it },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        )
                                    )
                                    Button(onClick = {
                                        sharedViewModel.searchCustomer(GuestHouseDatabase.getDatabase(context).customerDao())
                                    }) {
                                        Text(text = "Search")
                                    }

                                    if (sharedViewModel.errorMessage.isNotEmpty()) {
                                        Text(text = sharedViewModel.errorMessage, color = Color.Red)
                                    } else {
                                        if (sharedViewModel.searchResults.isNotEmpty()) {
                                            Text(text = "Customers Found:", fontWeight = FontWeight.Bold)
                                            LazyColumn {
                                                items(sharedViewModel.searchResults) { customer ->
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(8.dp)
                                                    ) {
                                                        Checkbox(
                                                            checked = sharedViewModel.selectedCustomers.contains(customer),
                                                            onCheckedChange = { isChecked ->
                                                                if (isChecked){
                                                                    sharedViewModel.addCustomer(customer)
                                                                    Toast.makeText(context,"Customer ${customer.name} Added ",Toast.LENGTH_SHORT).show()
                                                                }
                                                                else sharedViewModel.removeCustomer(customer)
                                                            }
                                                        )
                                                        Text(text = "${customer.name} (${customer.passportNo})")
                                                    }
                                                }
                                            }
                                            if (sharedViewModel.selectedCustomers.isNotEmpty()) {
                                                Button(onClick = { navController.navigate("booking_by_search") }) {
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
}






@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchRecordPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
        SearchRecord(sharedViewModel = SearchRecordViewModel(),
            navController = rememberNavController()
        )
    }
}