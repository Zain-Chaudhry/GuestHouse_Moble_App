package com.example.diamondguesthouse.userinterface.screens
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.userinterface.components.districtsMap
import com.example.diamondguesthouse.userinterface.components.provinces
import com.example.diamondguesthouse.userinterface.components.textfield.CustomTextField
import com.example.diamondguesthouse.userinterface.components.textfield.TimeField
import com.example.diamondguesthouse.userinterface.components.textfield.ValidationType
import com.example.diamondguesthouse.viewmodel.AddRecordViewModel
import com.example.diamondguesthouse.viewmodel.AddRecordViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddRecord(navController: NavController) {
    val viewModel: AddRecordViewModel = AddRecordViewModelFactory(LocalContext.current).create(
        AddRecordViewModel::class.java)
    val selectedTab = remember { mutableIntStateOf(0) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_arrorw_back),
                    contentDescription = null,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Text(
                    text = "Add Record",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null
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
                    // Tab Row
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item {
                            if (selectedTab.intValue == 0) {
                                LocalAddRecord(
                                    navController = navController,
                                    viewModel = viewModel)
                            } else {
                                ForeignAddRecord(
                                    navController = navController,
                                    viewModel = viewModel
                                    )
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun LocalAddRecord(navController: NavController, viewModel: AddRecordViewModel) {

    val gender = listOf("Male", "Female")
    val provinces = provinces
    val districts = districtsMap[viewModel.selectedProvince] ?: listOf("Please Select Province")
    val context = LocalContext.current

    CustomTextField(
        label = "CNIC",
        value = viewModel.cnic ,
        onValueChange = {viewModel.cnic = it},
        validationType = ValidationType.CNIC
    )

    CustomTextField(
        label = "Name" ,
        value = viewModel.name ,
        onValueChange ={viewModel.name = it}
    )

    CustomTextField(
        label = "Father's Name" ,
        value = viewModel.fatherName ,
        onValueChange ={viewModel.fatherName = it}
    )

        CustomTextField(
        label = "Permanent Address" ,
        value =viewModel.permanentAddress ,
        onValueChange ={viewModel.permanentAddress = it}
    )

    CustomDropdown(
        value = viewModel.selectedGender,
        label = "Gender",
        list = gender,
        onSelectedChange = {newGender ->
            viewModel.setGender(newGender)
        }
    )

    CustomTextField(
        label = "Cell No",
        value = viewModel.cellNo,
        onValueChange = { viewModel.cellNo = it },
        validationType = ValidationType.MOBILE
    )

    CustomDropdown(
        value = viewModel.selectedProvince,
        label = "Province",
        list = provinces,
        onSelectedChange = { newProvence ->
            viewModel.setProvince(newProvence)
        }
    )
    LaunchedEffect(viewModel.selectedProvince) {
        viewModel.selectedDistrict = "Please Select"
    }

    CustomDropdown(
        value = viewModel.selectedDistrict,
        label = "District",
        list = districts,
        onSelectedChange = { newDistrict ->
            viewModel.setDistrict(newDistrict) }
    )


    CustomTextField(
        label = "Purpose of Visit",
        value = viewModel.purposeOfVisit,
        onValueChange = { viewModel.purposeOfVisit = it }
    )


    CustomDateField(
        label = "Check in Date",
        value = viewModel.checkInDate,
        onDateSelected = {},
        isClickable = false
    )

    TimeField(
        label = "Check In Time",
        value = viewModel.checkInTime,
        onTimeSelected = {},
        isClickable = false
    )

    CustomDateField(
        label = "Check Out Date",
        value = viewModel.checkOutDate,
        onDateSelected = {viewModel.checkOutDate = it},
        isClickable = true
    )

    TimeField(
        label = "Check Out Time",
        value = viewModel.checkOutTime,
        onTimeSelected = {viewModel.checkOutTime = it},
        isClickable = true
    )

    CustomTextField(
        label = "Room No",
        value = viewModel.roomNo,
        onValueChange = { viewModel.roomNo = it }
    )
    CustomTextField(
        label = "Amount Received",
        value = viewModel.roomPrice,
        onValueChange = { viewModel.roomPrice = it  }
    )

    Button(
        onClick = {
           viewModel.submitLocalCustomer()
            Toast.makeText(context, "Customer Added", Toast.LENGTH_SHORT).show()
            navController.navigate("home")


        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Submit")
    }

}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun ForeignAddRecord(navController: NavController, viewModel: AddRecordViewModel){


    val gender = listOf("Male", "Female")
    val context = LocalContext.current

    CustomTextField(
        label = "Name" ,
        value = viewModel.name ,
        onValueChange ={viewModel.name = it}
    )

    CustomTextField(
        label = "Father's Name" ,
        value = viewModel.fatherName,
        onValueChange ={viewModel.fatherName = it}
    )
    CustomTextField(
        label = "Passport Number",
        value = viewModel.passportNo,
        onValueChange = {viewModel.passportNo = it},
        validationType = ValidationType.PASSPORT
    )

    // "+" symbol here


    CustomDateField(
        label = "Check Out Date",
        value = viewModel.visaUpTill,
        onDateSelected = {viewModel.visaUpTill = it},
        isClickable = true
    )

    CustomTextField(
        label = "Cell No",
        value = viewModel.cellNo,
        onValueChange = { viewModel.cellNo = it },
        validationType = ValidationType.MOBILE
    )

    // Gender here
    CustomDropdown(
        value = viewModel.selectedGender,
        label = "Gender",
        list = gender,
        onSelectedChange = {newGender ->
            viewModel.setGender(newGender)
        }
    )

    CustomTextField(
        label = "Permanent Address" ,
        value = viewModel.permanentAddress ,
        onValueChange ={viewModel.permanentAddress = it}
    )

    CustomTextField(
        label = "Country",
        value = viewModel.country,
        onValueChange = { viewModel.country = it }
    )

    CustomTextField(
        label = "Last Visited Country",
        value = viewModel.lastVisitedCountry,
        onValueChange = { viewModel.lastVisitedCountry = it }
    )

    CustomTextField(
        label = "NOC#",
        value = viewModel.noc,
        onValueChange = { viewModel.noc = it }
    )


    CustomTextField(
        label = "Purpose of Visit",
        value = viewModel.purposeOfVisit,
        onValueChange = { viewModel.purposeOfVisit = it }
    )

    CustomDateField(
        label = "Check in Date",
        value = viewModel.checkInDate,
        onDateSelected = {},
        isClickable = false
    )

    TimeField(
        label = "Check In Time",
        value = viewModel.checkInTime,
        onTimeSelected = {},
        isClickable = false
    )

    CustomDateField(
        label = "Check Out Date",
        value = viewModel.checkOutDate,
        onDateSelected = {viewModel.checkOutDate = it},
        isClickable = true
    )

    TimeField(
        label = "Check Out Time",
        value = viewModel.checkOutTime,
        onTimeSelected = {viewModel.checkOutTime = it},
        isClickable = true)

    CustomTextField(
        label = "Room No",
        value = viewModel.roomNo,
        onValueChange = { viewModel.roomNo = it }
    )
    CustomTextField(
        label = "Amount Received",
        value = viewModel.roomPrice,
        onValueChange = { viewModel.roomPrice = it }
    )
    Button(
        onClick = {
            viewModel.submitForeignCustomer()
            Toast.makeText(context, "Customer Added", Toast.LENGTH_SHORT).show()
            navController.navigate("home")


        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Submit")
    }

}
@Composable
fun CustomDropdown(
    value: String,
    label: String,
    list: List<String>,
    onSelectedChange: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    // Box to contain the dropdown and text field
    Box {
        Column {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedTextField(
                value = value,
                onValueChange = { /* No-op as it’s read-only */ },
//            label = { Text(text = label) },
                readOnly = true,
                trailingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded.value = !expanded.value })
                },
                modifier = Modifier.fillMaxWidth()

            )
            Spacer(modifier = Modifier.size(10.dp))
        }
        // Dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false } // Close when clicked outside
        ) {
            list.forEach{ item ->
                DropdownMenuItem(text = { Text(text = item, fontSize = 13.sp, fontWeight = FontWeight.Medium) }, onClick = {
                    onSelectedChange(item)
                    expanded.value = false
                })
            }

        }
    }
}

@Composable
fun CustomDateField(label: String, value: Long, onDateSelected: (date: Long) -> Unit, isClickable: Boolean) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Format the date for display (convert timestamp to a human-readable format)
    val formattedDate = if (value != 0L) {
        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(value))
    } else {
        "Please Select"
    }

    Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    Spacer(modifier = Modifier.size(10.dp))

    if (isClickable) {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = { /* No-op */ },
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(Calendar.YEAR, year)
                                calendar.set(Calendar.MONTH, month)
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                // Convert the selected date to a timestamp (Long)
                                val selectedDateInMillis = calendar.timeInMillis
                                onDateSelected(selectedDateInMillis)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        OutlinedTextField(
            value = formattedDate,
            onValueChange = { /* No-op */ },
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Spacer(modifier = Modifier.size(10.dp))
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddRecordPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
        AddRecord(rememberNavController())
    }
}
