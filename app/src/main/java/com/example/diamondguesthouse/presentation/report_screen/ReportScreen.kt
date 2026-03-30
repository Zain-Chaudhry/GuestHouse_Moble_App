package com.example.diamondguesthouse.presentation.report_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.appNavigation.NavCommand
import com.example.diamondguesthouse.appNavigation.OnGuestHouseNavigate
import com.example.diamondguesthouse.core.presentation.GenericTextView
import com.example.diamondguesthouse.presentation.add_record_screen.component.AddRecordDropdown
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportScreen(
    onNavigate: OnGuestHouseNavigate,
    viewModel: ReportScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val reportTypes = listOf("Daily Report", "Weekly Report", "Monthly Report")

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
                GenericTextView(
                    text = "Report",
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    AddRecordDropdown(
                        value = uiState.reportType,
                        label = "Please Select Report Type",
                        list = reportTypes,
                        onSelectedChange = { newType ->
                            viewModel.submitUserEvent(ReportUserEvent.ReportTypeChanged(newType))
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (uiState.reportType != "Please Select") {
                        GenericTextView(
                            text = "Selected Report Type: ${uiState.reportType}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GenericTextView(
                            text = "Total Check-Ins: ${uiState.totalCheckIns}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        GenericTextView(
                            text = "Total Income: ${uiState.totalIncome} Rs.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GenericTextView(text = "Report Generated Successfully!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        GenericTextView(text = "Thank you for using our application!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        GenericTextView(text = "Have a great day!", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
