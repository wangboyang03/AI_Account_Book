package cn.itcast.ai_account_book.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

object Routes {
  const val HOME = "home"
  const val CHART = "chart"
  const val CALENDAR = "calendar"
  const val SETTINGS = "settings"
}

@Composable
fun HomeScreen(
  vm: HomeViewModel = viewModel(
    factory = viewModelFactory {
      addInitializer(HomeViewModel::class) { HomeViewModel() }
    }
  )
) {
  val navController = rememberNavController()
  val backStack by navController.currentBackStackEntryAsState()
  val currentRoute = backStack?.destination?.route ?: Routes.HOME

  Scaffold(
    containerColor = Color.Transparent,
    floatingActionButton = {
      FloatingActionButton(
        onClick = { },
        containerColor = Color(0xFF7F3DFF),
        contentColor = Color.White
      ) {
        Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
      }
    },
    bottomBar = {
      NavigationBar(containerColor = Color.White) {
        listOf(
          Routes.HOME to "Home",
          Routes.CHART to "Chart",
          Routes.CALENDAR to "Calendar",
          Routes.SETTINGS to "Settings"
        ).forEach { (route, label) ->
          NavigationBarItem(
            selected = currentRoute == route,
            onClick = {
              navController.navigate(route) {
                popUpTo(Routes.HOME) { saveState = true }
                launchSingleTop = true
                restoreState = true
              }
            },
            icon = {
              Text(label, fontSize = 11.sp)
            },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = Color(0xFF7F3DFF),
              indicatorColor = Color(0xFFEEE5FF)
            )
          )
        }
      }
    }
  ) { padding ->
    NavHost(
      navController = navController,
      startDestination = Routes.HOME,
      modifier = Modifier.padding(padding)
    ) {
      composable(Routes.HOME) { HomeTabPage(vm) }
      composable(Routes.CHART) { PlaceholderPage("Chart") }
      composable(Routes.CALENDAR) { PlaceholderPage("Calendar") }
      composable(Routes.SETTINGS) { PlaceholderPage("Settings") }
    }
  }
}

@Composable
private fun HomeTabPage(vm: HomeViewModel) {
  val state = vm.uiState

  Column(
    Modifier.fillMaxSize()
      .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFEFF1F5))))
  ) {
    // 问候 + 诗词
    Column(Modifier.padding(start = 24.dp, top = 20.dp, bottom = 12.dp)) {
      Text(
        "${state.userName}，欢迎回来",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF23233C)
      )
      Spacer(Modifier.height(6.dp))
      Text(state.poem, fontSize = 13.sp, color = Color(0xFF9E9EB8))
    }

    // 卡片区
    Row(
      Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      state.cards.forEachIndexed { i, card ->
        Card(
          Modifier.width(150.dp).height(90.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = if (i == 1) Color(0xFF7F3DFF) else Color.White
          ),
          elevation = CardDefaults.cardElevation(4.dp)
        ) {
          Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
              card.label,
              fontSize = 12.sp,
              color = if (i == 1) Color(0xFFE0D6FF) else Color(0xFF9E9EB8)
            )
            Text(
              "$${"%.2f".format(card.amount)}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = if (i == 1) Color.White else Color(0xFF23233C)
            )
          }
        }
      }
    }

    Spacer(Modifier.height(20.dp))

    // Latest Entries
    Row(
      Modifier.fillMaxWidth().padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("Latest Entries", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23233C))
      Text("•••", fontSize = 16.sp, color = Color(0xFF7F3DFF))
    }
    Spacer(Modifier.height(12.dp))
    Column(
      Modifier.weight(1f).padding(horizontal = 24.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      state.transactions.forEach { tx ->
        Card(
          Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(2.dp)
        ) {
          Row(
            Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(tx.category, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF23233C))
              Text("${tx.date}  •  ${tx.method}", fontSize = 11.sp, color = Color(0xFF9E9EB8))
            }
            Text(
              "- $${"%.0f".format(tx.amount)} + Vat ${tx.vat}%",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFFE53E3E)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PlaceholderPage(title: String) {
  Box(Modifier.fillMaxSize().background(Color(0xFFEFF1F5)), contentAlignment = Alignment.Center) {
    Text(title, fontSize = 20.sp, color = Color(0xFF9E9EB8))
  }
}
