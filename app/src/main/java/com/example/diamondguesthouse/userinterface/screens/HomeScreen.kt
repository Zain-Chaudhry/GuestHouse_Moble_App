package com.example.diamondguesthouse.userinterface.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.diamondguesthouse.NavRoutes
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.ui.theme.Zinc
import com.example.diamondguesthouse.viewmodel.HomeViewModel
import com.example.diamondguesthouse.viewmodel.HomeViewModelFactory

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: HomeViewModel = HomeViewModelFactory(context).create(HomeViewModel::class.java)

  Surface(modifier = Modifier.fillMaxSize()) {
      ConstraintLayout(modifier = Modifier.fillMaxSize()) {
          val (nameRow, topBar, list, card) = createRefs()
          Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
              modifier = Modifier.constrainAs(topBar) {
                  top.linkTo(parent.top)
                  start.linkTo(parent.start)
                  end.linkTo(parent.end)
              })
          Row(modifier = Modifier
              .fillMaxWidth()
              .padding(top = 65.dp, start = 16.dp, end = 16.dp)
              .constrainAs(nameRow) {
                  top.linkTo(parent.top)
                  start.linkTo(parent.start)

              }) {
              Column {
                  val greeting = viewModel.getGreetingMessage()
                  Text(text = greeting , style = MaterialTheme.typography.bodyLarge, color = Color.White)
                  Text(text = "Welcome to Diamond Guesthouse", style = MaterialTheme.typography.labelLarge, color = Color.White)
              }
          }

          val state = viewModel.roomsWithCustomers.collectAsState(initial = emptyList())
          val monthlyIncome = viewModel.getMonthlyIncome(state.value)
          val checkIns = viewModel.getCheckIns(state.value)
          val checkOuts = viewModel.getCheckOuts(state.value)

          CardItem(modifier = Modifier
              .constrainAs(card){
                  top.linkTo(nameRow.bottom)
                  start.linkTo(parent.start)
                  end.linkTo(parent.end)
              }, monthlyIncome, checkIns, checkOuts, navController)
          Column(modifier = Modifier.constrainAs(list){
              top.linkTo(card.bottom, margin = 30.dp)
              start.linkTo(parent.start)
              end.linkTo(parent.end)
              bottom.linkTo(parent.bottom)
              height = Dimension.fillToConstraints
          }) {
              Row {
                  CardButtons(
                      title = "New Booking",
                      image = R.drawable.ic_add_icon,
                      modifier = Modifier.align(Alignment.Top),
                      navController = navController,
                      navigateTo = NavRoutes.ADD_RECORD)
                  CardButtons(
                      title = "Search Record",
                      image = R.drawable.ic_search,
                      modifier = Modifier.align(Alignment.Top),
                      navController = navController,
                      navigateTo = NavRoutes.SEARCH_RECORD)
              }
              Row {
                  CardButtons(title = "View Report",
                      image = R.drawable.ic_month,
                      modifier = Modifier.align(Alignment.Top),
                      navController = navController,
                      navigateTo = NavRoutes.REPORT_SCREEN)
                  CardButtons(
                      title = "View Booking",
                      image = R.drawable.ic_month,
                      modifier = Modifier.align(Alignment.Top),
                      navController = navController,
                      navigateTo = NavRoutes.VIEW_BOOKING)
              }
          }
      }
  }
}

@Composable
fun CardItem(modifier: Modifier, monthlyIncome: String, checkIns: String, checkOuts: String, navController: NavController) {
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = "Total Revenue",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = monthlyIncome,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold)
            }
            Image(painter = painterResource(id = R.drawable.ic_settings), contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd).clickable { navController. navigate(NavRoutes.SETTING_SCREEN) } )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {

            Column {
                Spacer(modifier = Modifier.size(20.dp))
                CardRowItem(
                    image = R.drawable.ic_down_circle_arrow,
                    title = "Today's Check Ins",
                    rooms = checkIns,
                    navController = navController,
                    navigateTo = NavRoutes.VIEW_BOOKING)
                Spacer(modifier = Modifier.size(10.dp))
                CardRowItem(
                    image = R.drawable.ic_up_circle_arrow,
                    title ="Today's Check Outs",
                    rooms = checkOuts,
                    navController = navController,
                    navigateTo = NavRoutes.VIEW_CHECKOUT)
            }



        }
    }
}

@Composable
fun CardRowItem(image: Int, title: String, rooms: String, navController: NavController, navigateTo: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(navigateTo) },
        verticalAlignment = Alignment.CenterVertically ){
        Image(painter = painterResource(id = image), contentDescription = null )

        Spacer(modifier = Modifier.size(8.dp))

        Text(text = title, fontSize = 16.sp, color = Color.White)

        Spacer(modifier = Modifier.weight(1f))

        Text(text = rooms, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun CardButtons(title: String, image: Int, modifier: Modifier, navController: NavController, navigateTo: String) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .width(150.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { navController.navigate(navigateTo) }
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.matchParentSize().align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 8.dp) // Add padding as needed
                )
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {

    DiamondGuestHouseTheme(isSystemInDarkTheme()){
       HomeScreen(rememberNavController())
    }
}