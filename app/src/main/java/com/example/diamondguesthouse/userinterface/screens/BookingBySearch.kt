package com.example.diamondguesthouse.userinterface.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.diamondguesthouse.userinterface.components.textfield.CustomDateField
import com.example.diamondguesthouse.userinterface.components.textfield.CustomTextField
import com.example.diamondguesthouse.userinterface.components.textfield.TimeField
import com.example.diamondguesthouse.viewmodel.SearchRecordViewModel

@Composable
fun BookingBySearch(sharedViewModel: SearchRecordViewModel, navController: NavController) {
    val context = LocalContext.current


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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrorw_back),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart) // Align to the start (left)
                        .clickable { navController.popBackStack() }
                )

                Text(
                    text = "New Booking",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center) // Center the text in the middle
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    item {
                        CustomerForm(viewModel = sharedViewModel)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Customers:",fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.size(10.dp))

                    }
                    if (sharedViewModel.selectedCustomers.isEmpty()) {
                        item {
                            Text(text = "No customers selected", modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        items(sharedViewModel.selectedCustomers) { customer ->
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Text(text = "${sharedViewModel.selectedCustomers.indexOf(customer) + 1}. ${customer.name}")
                            }
                        }
                    }
                item {

                    Button(onClick = {
                        sharedViewModel.confirmBooking(GuestHouseDatabase.getDatabase(context).roomDao()
                        )
                        Toast.makeText(context, "Record Added", Toast.LENGTH_SHORT).show()
                        navController.navigate("home")

                    }) {
                        Text(text = "Confirm Booking")

                    }
                }
                }




            }

            }
        }
    }



@Composable
fun CustomerForm( viewModel: SearchRecordViewModel) {
    val roomNum =
        listOf("110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120")

    CustomDropdown(
        value = viewModel.roomNo,
        label = "Room No.",
        list = roomNum,
        onSelectedChange = { newRoom ->
            viewModel.roomNo = newRoom
        },
        isAvailable = true
    )

    CustomDateField(
        label = "Check Out Date",
        value = viewModel.checkOutDate,
        onDateSelected = { viewModel.checkOutDate = it },
        isClickable = true
    )

    TimeField(
        label = "Check Out Time",
        value = viewModel.checkOutTime,
        onTimeSelected = { viewModel.checkOutTime = it },
        isClickable = true
    )

    CustomTextField(
        label = "Amount Received",
        value = viewModel.roomPrice,
        onValueChange = { viewModel.roomPrice = it },
        imeAction = ImeAction.Next, // Define next action
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BookingBySearchPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
       BookingBySearch(sharedViewModel = SearchRecordViewModel(),
           navController = rememberNavController())
    }
}