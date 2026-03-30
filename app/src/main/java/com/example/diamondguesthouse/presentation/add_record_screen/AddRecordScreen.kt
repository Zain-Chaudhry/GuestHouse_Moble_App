package com.example.diamondguesthouse.presentation.add_record_screen

import com.example.diamondguesthouse.core.utils.showToast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.appNavigation.NavCommand
import com.example.diamondguesthouse.appNavigation.OnGuestHouseNavigate
import com.example.diamondguesthouse.appNavigation.GuestHouseNavKey
import com.example.diamondguesthouse.domain.models.CustomerDraftModel
import com.example.diamondguesthouse.presentation.add_record_screen.component.AddRecordDropdown
import com.example.diamondguesthouse.core.presentation.GuestHouseBackground
import com.example.diamondguesthouse.core.presentation.CustomDateField
import com.example.diamondguesthouse.core.presentation.CustomTextField
import com.example.diamondguesthouse.core.presentation.GenericTextView
import com.example.diamondguesthouse.core.presentation.TimeField
import com.example.diamondguesthouse.core.presentation.ValidationType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddRecordScreen(
    onNavigate: OnGuestHouseNavigate,
    viewModel: AddRecordScreenViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val selectedTab = remember { mutableIntStateOf(0) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { ev ->
            when (ev) {
                is AddRecordUiEvent.Message -> context.showToast(ev.text)
                AddRecordUiEvent.NavigateHome -> onNavigate(NavCommand.ReplaceRoot(GuestHouseNavKey.Home))
            }
        }
    }

    LaunchedEffect(uiState.customerStatus) {
        when (val s = uiState.customerStatus) {
            is CustomerSubmitStatus.AlreadyCheckedIn -> context.showToast(s.message)
            is CustomerSubmitStatus.Error -> context.showToast(s.message)
            is CustomerSubmitStatus.CustomerAdded -> {
                context.showToast(s.message)
                onNavigate(NavCommand.ReplaceRoot(GuestHouseNavKey.Home))
                viewModel.submitUserEvent(AddRecordUserEvent.ResetCustomerStatus)
            }
            else -> Unit
        }
    }

    GuestHouseBackground { _ ->
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
                GenericTextView(
                    text = stringResource(R.string.add_record),
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
                Column {
                    PrimaryTabRow(
                        selectedTabIndex = selectedTab.intValue,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Tab(
                            modifier = Modifier.weight(1f),
                            selected = selectedTab.intValue == 0,
                            onClick = { selectedTab.intValue = 0 },
                            text = {
                                Text(
                                    text = stringResource(R.string.local),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LocalContentColor.current,
                                )
                            },
                        )
                        Tab(
                            modifier = Modifier.weight(1f),
                            selected = selectedTab.intValue == 1,
                            onClick = { selectedTab.intValue = 1 },
                            text = {
                                Text(
                                    text = stringResource(R.string.foreigner),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = LocalContentColor.current,
                                )
                            },
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        item {
                            if (selectedTab.intValue == 0) {
                                AddRecordCustomerForm(viewModel, uiState, isLocal = true)
                            } else {
                                AddRecordCustomerForm(viewModel, uiState, isLocal = false)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddRecordCustomerForm(
    viewModel: AddRecordScreenViewModel,
    uiState: AddRecordUiState,
    isLocal: Boolean,
) {
    val roomNum = listOf("110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120")
    AddRecordDropdown(
        value = uiState.roomNo,
        label = stringResource(R.string.room_no),
        list = roomNum,
        onSelectedChange = { viewModel.submitUserEvent(AddRecordUserEvent.RoomNoChanged(it)) },
        isAvailable = true,
    )
    CustomDateField(
        label = stringResource(R.string.check_in_date),
        value = uiState.checkInDate,
        onDateSelected = {},
        isClickable = false,
    )
    TimeField(
        label = stringResource(R.string.check_in_time),
        value = uiState.checkInTime,
        onTimeSelected = {},
        isClickable = false,
    )
    CustomDateField(
        label = stringResource(R.string.check_out_date),
        value = uiState.checkOutDate,
        onDateSelected = { viewModel.submitUserEvent(AddRecordUserEvent.CheckOutDateChanged(it)) },
        isClickable = true,
    )
    TimeField(
        label = stringResource(R.string.check_out_time),
        value = uiState.checkOutTime,
        onTimeSelected = { viewModel.submitUserEvent(AddRecordUserEvent.CheckOutTimeChanged(it)) },
        isClickable = true,
    )
    CustomTextField(
        label = stringResource(R.string.amount_received),
        value = uiState.roomPrice,
        onValueChange = { viewModel.submitUserEvent(AddRecordUserEvent.RoomPriceChanged(it)) },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    if (isLocal) {
        Image(
            painter = painterResource(id = R.drawable.ic_add_person),
            contentDescription = null,
            modifier = Modifier
                .clickable { viewModel.submitUserEvent(AddRecordUserEvent.AddCustomerRow) }
                .padding(vertical = 10.dp),
        )
        Column {
            uiState.customerRows.forEachIndexed { index, draft ->
                LocalDraftFields(viewModel, draft, index)
            }
        }
        if (uiState.customerRows.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove_person),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.submitUserEvent(AddRecordUserEvent.RemoveCustomerRow(uiState.customerRows.lastIndex))
                    }
                    .padding(vertical = 10.dp),
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_add_person),
            contentDescription = null,
            modifier = Modifier
                .clickable { viewModel.submitUserEvent(AddRecordUserEvent.AddCustomerRow) }
                .padding(vertical = 10.dp),
        )
        Column {
            uiState.customerRows.forEachIndexed { index, draft ->
                ForeignDraftFields(viewModel, draft, index)
            }
        }
        if (uiState.customerRows.isNotEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_remove_person),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        viewModel.submitUserEvent(AddRecordUserEvent.RemoveCustomerRow(uiState.customerRows.lastIndex))
                    }
                    .padding(vertical = 10.dp),
            )
        }
    }
    Button(
        onClick = { viewModel.submitUserEvent(AddRecordUserEvent.SubmitRecord) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
    ) {
        GenericTextView("Submit")
    }
}

@Composable
private fun LocalDraftFields(viewModel: AddRecordScreenViewModel, draft: CustomerDraftModel, index: Int) {
    val gender = listOf("Male", "Female")
    fun update(transform: (CustomerDraftModel) -> CustomerDraftModel) {
        viewModel.submitUserEvent(AddRecordUserEvent.UpdateCustomerDraft(index, transform(draft)))
    }
    CustomTextField(
        label = stringResource(R.string.cnic),
        value = draft.cnic ?: "",
        onValueChange = { v -> update { d -> d.copy(cnic = v.ifBlank { null }) } },
        validationType = ValidationType.CNIC,
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    CustomTextField(
        label = stringResource(R.string.name),
        value = draft.name,
        onValueChange = { v -> update { d -> d.copy(name = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomTextField(
        label = stringResource(R.string.father_s_name),
        value = draft.fatherName,
        onValueChange = { v -> update { d -> d.copy(fatherName = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomTextField(
        label = stringResource(R.string.permanent_address),
        value = draft.permanentAddress,
        onValueChange = { v -> update { d -> d.copy(permanentAddress = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    AddRecordDropdown(
        value = draft.selectedGender,
        label = "Gender",
        list = gender,
        onSelectedChange = { v -> update { d -> d.copy(selectedGender = v) } },
    )
    CustomTextField(
        label = stringResource(R.string.cell_no),
        value = draft.cellNo,
        onValueChange = { v -> update { d -> d.copy(cellNo = v) } },
        validationType = ValidationType.MOBILE,
        imeAction = ImeAction.Done,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Composable
private fun ForeignDraftFields(viewModel: AddRecordScreenViewModel, draft: CustomerDraftModel, index: Int) {
    val gender = listOf("Male", "Female")
    fun update(transform: (CustomerDraftModel) -> CustomerDraftModel) {
        viewModel.submitUserEvent(AddRecordUserEvent.UpdateCustomerDraft(index, transform(draft)))
    }
    CustomTextField(
        label = stringResource(R.string.name),
        value = draft.name,
        onValueChange = { v -> update { d -> d.copy(name = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomTextField(
        label = stringResource(R.string.father_s_name),
        value = draft.fatherName,
        onValueChange = { v -> update { d -> d.copy(fatherName = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomTextField(
        label = stringResource(R.string.passport_number),
        value = draft.passportNo ?: "",
        onValueChange = { v -> update { d -> d.copy(passportNo = v.ifBlank { null }) } },
        validationType = ValidationType.PASSPORT,
        imeAction = ImeAction.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomDateField(
        label = stringResource(R.string.visa_up_till),
        value = draft.visaUpTill ?: 0L,
        onDateSelected = { t -> update { d -> d.copy(visaUpTill = t) } },
        isClickable = true,
    )
    CustomTextField(
        label = "Cell No",
        value = draft.cellNo,
        onValueChange = { v -> update { d -> d.copy(cellNo = v) } },
        validationType = ValidationType.MOBILE,
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
    AddRecordDropdown(
        value = draft.selectedGender,
        label = "Gender",
        list = gender,
        onSelectedChange = { v -> update { d -> d.copy(selectedGender = v) } },
    )
    CustomTextField(
        label = stringResource(R.string.permanent_address),
        value = draft.permanentAddress,
        onValueChange = { v -> update { d -> d.copy(permanentAddress = v) } },
        imeAction = ImeAction.Next,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
    CustomTextField(
        label = stringResource(R.string.country),
        value = draft.country ?: "",
        onValueChange = { v -> update { d -> d.copy(country = v.ifBlank { null }) } },
        imeAction = ImeAction.Done,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}
