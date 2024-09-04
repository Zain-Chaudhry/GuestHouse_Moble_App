package com.example.diamondguesthouse.userinterface.screens

//import androidx.compose.foundation.layout.BoxScopeInstance.align
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


@Composable
fun AddRecord(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(1) }

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBar, nameRow, surface) = createRefs()

            // Top Bar Image
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            // Name Row containing back arrow, title, and notification icon
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

            // Rounded Corner Surface
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
                        selectedTabIndex = selectedTab
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Local") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Foreigner") }
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        item {
                            if (selectedTab == 0) {
                                LocalAddRecord()
                            } else {
                                ForeignAddRecord()
                            }
                        }

                        // Submit Button
                        item {
                            Button(
                                onClick = { /* Handle form submission */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Submit")
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf(value) }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun CustomDropdown(
    value: String,
    label: String,
    list: List<String>,
    onSelectedChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Box to contain the dropdown and text field
    Box {
        OutlinedTextField(
            value = value, // This will show the selected gender
            onValueChange = { /* No-op as it’s read-only */ },
            label = { Text(text = label) }, // Label for the text field
            readOnly = true, // Make it read-only to avoid direct input
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_arrow_drop_down), contentDescription = null,
                    modifier = Modifier.clickable { expanded = !expanded  })
            },
            modifier = Modifier.fillMaxWidth()

        )
        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Close when clicked outside
        ) {
            list.forEach{ item ->
                DropdownMenuItem(text = { Text(text = item, fontSize = 13.sp, fontWeight = FontWeight.Medium) }, onClick = {
                    onSelectedChange(item)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun LocalAddRecord() {
    var localName by remember { mutableStateOf("") }
    var localFatherName by remember { mutableStateOf("") }
    var localCellNo by remember { mutableStateOf("") }
    var cnic by remember { mutableStateOf("") }
    var localPermanentAddress by remember { mutableStateOf("") }
    var localTemporaryAddress by remember { mutableStateOf("") }
    var localPurposeOfVisit by remember { mutableStateOf("") }
    var localRoomNo by remember { mutableStateOf("") }
    val gender = listOf("Male", "Female")
    var selectedGenderLocal by remember { mutableStateOf("Please Select") }
    val provinces = provinces
    var selectedProvince by remember { mutableStateOf("Please Select") }
    var selectedDistrict by remember { mutableStateOf("Please Select") }
    val districts = districtsMap[selectedProvince] ?: listOf("Please Select Province")
    var temporaryProvince by remember { mutableStateOf("Please Select") }
    var temporaryDistrict by remember { mutableStateOf("Please Select") }
    val tempDistricts = districtsMap[temporaryDistrict] ?: listOf("Please Select  Temporary Province")

    CustomTextField(
        label = "CNIC",
        value = cnic ,
        onValueChange = {cnic = it}
    )

    CustomTextField(
        label = "Name" ,
        value = localName ,
        onValueChange ={localName = it}
    )

    CustomTextField(
        label = "Father's Name" ,
        value = localFatherName ,
        onValueChange ={localFatherName = it}
    )

    // "+" sign here

    CustomTextField(
        label = "Permanet Address" ,
        value = localPermanentAddress ,
        onValueChange ={localPermanentAddress = it}
    )

    // Gender here


    CustomDropdown(
        value = selectedGenderLocal,
        label = "Gender",
        list = gender,
        onSelectedChange = {selectedGenderLocal = it})


    CustomTextField(
        label = "Cell No",
        value = localCellNo,
        onValueChange = { localCellNo = it }
    )

    // provence

    CustomDropdown(
        value = selectedProvince,
        label = "Province",
        list = com.example.diamondguesthouse.userinterface.components.provinces.sorted(),
        onSelectedChange = { newProvence ->
            selectedProvince = newProvence
            selectedDistrict = "Please Select"
        }
    )
    // Reset selectedDistrict when selectedProvince changes
    LaunchedEffect(selectedProvince) {
        selectedDistrict = "Please Select"
    }

    CustomDropdown(
        value = selectedDistrict,
        label = "District",
        list = districts,
        onSelectedChange = { newDistrct ->
            selectedDistrict = newDistrct }
    )


    CustomTextField(
        label = "Temporary Address",
        value = localTemporaryAddress,
        onValueChange = { localTemporaryAddress = it }
    )

    // Temporary Provience
    CustomDropdown(
        value = temporaryProvince,
        label = "Temporary Province",
        list = com.example.diamondguesthouse.userinterface.components.provinces.sorted(),
        onSelectedChange = { newTempProvence ->
            temporaryProvince = newTempProvence
            temporaryDistrict = "Please Select"
        }
    )

    LaunchedEffect(temporaryProvince) {
        temporaryDistrict = "Please Select"
    }

    // Temporary Distric

    CustomDropdown(
        value = temporaryDistrict,
        label = "Temporary District",
        list = tempDistricts,
        onSelectedChange = { newTempDistrct ->
            temporaryDistrict = newTempDistrct }
    )




    // total of Accompaning Guest

    CustomTextField(
        label = "Purpose of Visit",
        value = localPurposeOfVisit,
        onValueChange = { localPurposeOfVisit = it }
    )

    /* Check in date
    * Check in time
    * check out date
    * check out time
    * */

    CustomTextField(
        label = "Room No",
        value = localRoomNo,
        onValueChange = { localRoomNo = it }
    )
}


@Composable
fun ForeignAddRecord() {
    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var cellNo by remember { mutableStateOf("") }
    var permanentAddress by remember { mutableStateOf("") }
    var purposeOfVisit by remember { mutableStateOf("") }
    var roomNo by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var lastVisitedCountry by remember { mutableStateOf("") }
    var passportNo by remember { mutableStateOf("") }
    var noc by remember { mutableStateOf("") }

    CustomTextField(
        label = "Name" ,
        value = name ,
        onValueChange ={name = it}
    )

    CustomTextField(
        label = "Father's Name" ,
        value = fatherName ,
        onValueChange ={fatherName = it}
    )
    CustomTextField(
        label = "Passort Number",
        value = passportNo,
        onValueChange = {passportNo = it}
    )

    // "+" symbol here

    // Visa up till here

    CustomTextField(
        label = "Cell No",
        value = cellNo,
        onValueChange = { cellNo = it }
    )

    // Gender here

    CustomTextField(
        label = "Permanet Address" ,
        value = permanentAddress ,
        onValueChange ={permanentAddress = it}
    )

    CustomTextField(
        label = "Country",
        value = country,
        onValueChange = { country = it }
    )

    CustomTextField(
        label = "Last Visited Country",
        value = lastVisitedCountry,
        onValueChange = { lastVisitedCountry = it }
    )

    CustomTextField(
        label = "NOC#",
        value = noc,
        onValueChange = { noc = it }
    )

    // Total Guest here

    CustomTextField(
        label = "Purpose of Visit",
        value = purposeOfVisit,
        onValueChange = { purposeOfVisit = it }
    )

    /* Check in date
    * Check in time
    * check out date
    * check out time
    * */
    CustomTextField(
        label = "Room No",
        value = roomNo,
        onValueChange = { roomNo = it }
    )
}



@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddRecordPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
        AddRecord(rememberNavController())
    }
}
