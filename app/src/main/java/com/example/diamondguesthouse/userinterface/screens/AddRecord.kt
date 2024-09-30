package com.example.diamondguesthouse.userinterface.screens
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
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
import com.example.diamondguesthouse.NavRoutes
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.data.GuestHouseDatabase
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.userinterface.components.textfield.CustomDateField
import com.example.diamondguesthouse.userinterface.components.textfield.CustomTextField
import com.example.diamondguesthouse.userinterface.components.textfield.TimeField
import com.example.diamondguesthouse.userinterface.components.textfield.ValidationType
import com.example.diamondguesthouse.viewmodel.AddRecordViewModel
import com.example.diamondguesthouse.viewmodel.CustomerEntityData
import com.example.diamondguesthouse.viewmodel.CustomerStatus

@Composable
fun AddRecord(viewModel: AddRecordViewModel, navController: NavController) {

    val context = LocalContext.current
    val selectedTab = remember { mutableIntStateOf(0) }

    val customerStatus = viewModel.customerStatus.observeAsState()

    LaunchedEffect(customerStatus.value) {
        val status = customerStatus.value
        Log.d("AddRecord", "Customer Status: $status")
        when(customerStatus.value) {
            is CustomerStatus.AlreadyCheckedIn -> Toast.makeText(context, (status as CustomerStatus.AlreadyCheckedIn).message, Toast.LENGTH_SHORT).show()
            is CustomerStatus.Error -> Toast.makeText(
                context,
                (status as CustomerStatus.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            is CustomerStatus.CustomerAdded -> {
                Toast.makeText(context, (status as CustomerStatus.CustomerAdded).message, Toast.LENGTH_SHORT).show()
                navController.navigate(NavRoutes.HOME){popUpTo(0){inclusive = true} }
                viewModel.resetCustomerStatus()
            }
            else -> Unit
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
                    text = "Add Record",
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item {
                            if (selectedTab.intValue == 0) {
                                CustomerForm(viewModel = viewModel, isLocal = true)
                            } else{
                                CustomerForm(viewModel = viewModel, isLocal = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerForm(viewModel: AddRecordViewModel, isLocal: Boolean = true) {
    val roomNum = listOf("110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120")
    val context = LocalContext.current

    CustomDropdown(
        value = viewModel.roomNo,
        label = "Room No.",
        list = roomNum,
        onSelectedChange = {newRoom ->
            viewModel.roomNo = newRoom},
        isAvailable = true
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
        label = "Amount Received",
        value = viewModel.roomPrice,
        onValueChange = { viewModel.roomPrice = it  },
        imeAction = ImeAction.Next, // Define next action
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    if(isLocal) {

        Image(
            painter = painterResource(id = R.drawable.ic_add_person),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    viewModel.addCustomerRecord()

                }
                .padding(vertical = 10.dp)

        )

        // Display list of customer records
        Column {
            viewModel.customerRecords.forEachIndexed { index, record ->
                LocalAddRecord(viewModel, record, index)
            }
        }

        if (viewModel.customerRecords.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove_person),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.removeCustomerRecord(viewModel.customerRecords.size - 1)
                    }
                    .padding(vertical = 10.dp)

            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_add_person),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    viewModel.addCustomerRecord()

                }
                .padding(vertical = 10.dp)

        )

        // Display list of customer records
        Column {
            viewModel.customerRecords.forEachIndexed { index, record ->
                ForeignAddRecord(viewModel, record, index)
            }
        }

        if (viewModel.customerRecords.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove_person),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.removeCustomerRecord(viewModel.customerRecords.size - 1)
                    }
                    .padding(vertical = 10.dp)

            )
        }

    }

    // Submit button
    Button(onClick = {
        viewModel.submitRecord(
            roomDao = GuestHouseDatabase.getDatabase(context).roomDao(),
            customerDao = GuestHouseDatabase.getDatabase(context).customerDao()
        )



    }, modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 30.dp)) {
        Text("Submit")
    }
    
}


@SuppressLint("AutoboxingStateCreation")
@Composable
fun LocalAddRecord(viewModel: AddRecordViewModel, customerRecord: CustomerEntityData, recordIndex: Int) {

    val gender = listOf("Male", "Female")
    remember { FocusRequester() }

    CustomTextField(
        label = "CNIC",
        value = customerRecord.cnic.value?: ""  ,
        onValueChange = {viewModel.customerRecords[recordIndex].cnic.value = it},
        validationType = ValidationType.CNIC,
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    CustomTextField(
        label = "Name" ,
        value = customerRecord.name.value ,
        onValueChange ={viewModel.customerRecords[recordIndex].name.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomTextField(
        label = "Father's Name" ,
        value = customerRecord.fatherName.value ,
        onValueChange ={viewModel.customerRecords[recordIndex].fatherName.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomTextField(
        label = "Permanent Address" ,
        value = customerRecord.permanentAddress.value ,
        onValueChange ={viewModel.customerRecords[recordIndex].permanentAddress.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomDropdown(
        value = customerRecord.selectedGender.value,
        label = "Gender",
        list = gender,
        onSelectedChange = { newGender ->
            viewModel.customerRecords[recordIndex].selectedGender.value = newGender
        }
    )

    CustomTextField(
        label = "Cell No",
        value = customerRecord.cellNo.value,
        onValueChange = { viewModel.customerRecords[recordIndex].cellNo.value = it },
        validationType = ValidationType.MOBILE,
        imeAction = ImeAction.Done,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun ForeignAddRecord(viewModel: AddRecordViewModel, customerRecord: CustomerEntityData, recordIndex: Int){


    val gender = listOf("Male", "Female")
    remember { FocusRequester() }

    CustomTextField(
        label = "Name" ,
        value = customerRecord.name.value ,
        onValueChange ={viewModel.customerRecords[recordIndex].name.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomTextField(
        label = "Father's Name" ,
        value = customerRecord.fatherName.value,
        onValueChange ={viewModel.customerRecords[recordIndex].fatherName.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    CustomTextField(
        label = "Passport Number",
        value = customerRecord.passportNo.value?: "",
        onValueChange = {viewModel.customerRecords[recordIndex].passportNo.value = it},
        validationType = ValidationType.PASSPORT,
        imeAction = ImeAction.None,
//
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomDateField(
        label = " Visa up till",
        value = customerRecord.visaUpTill.value?: 0L,
        onDateSelected = {viewModel.customerRecords[recordIndex].visaUpTill.value = it},
        isClickable = true
    )

    CustomTextField(
        label = "Cell No",
        value =  customerRecord.cellNo.value,
        onValueChange = { viewModel.customerRecords[recordIndex].cellNo.value = it },
        validationType = ValidationType.MOBILE,
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    // Gender here
    CustomDropdown(
        value = customerRecord.selectedGender.value,
        label = "Gender",
        list = gender,
        onSelectedChange = {newGender ->
            viewModel.customerRecords[recordIndex].selectedGender.value = newGender
        }
    )

    CustomTextField(
        label = "Permanent Address" ,
        value = customerRecord.permanentAddress.value ,
        onValueChange ={viewModel.customerRecords[recordIndex].permanentAddress.value = it},
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )

    CustomTextField(
        label = "Country",
        value = customerRecord.country.value?: "",
        onValueChange = { viewModel.customerRecords[recordIndex].country.value = it },
        imeAction = ImeAction.Done,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )


}
@Composable
fun CustomDropdown(
    value: String,
    label: String,
    list: List<String>,
    onSelectedChange: (String) -> Unit,
    isAvailable: Boolean = true
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
        if (!isAvailable) {
            Toast.makeText(LocalContext.current, "Please Select", Toast.LENGTH_SHORT).show()

        }
    }
}




@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddRecordPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
        AddRecord(viewModel = AddRecordViewModel(), navController = rememberNavController())
    }
}


