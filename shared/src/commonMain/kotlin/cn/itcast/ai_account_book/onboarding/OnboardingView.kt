package cn.itcast.ai_account_book.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreen(
  onFinished: () -> Unit = {},
  vm: OnboardingViewModel = remember { OnboardingViewModel() }
) {
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState { pages.size }

  LaunchedEffect(pagerState.currentPage) {
    vm.onPageChanged(pagerState.currentPage)
  }

  Column(
    Modifier.fillMaxSize()
      .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFEFF1F5))))
      .safeContentPadding()
  ) {
    HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { i ->
      Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Image(painterResource(pages[i].icon), null, Modifier.height(200.dp))
        Spacer(Modifier.height(48.dp))
        Text(
          pages[i].title,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF23233C)
        )
        Spacer(Modifier.height(14.dp))
        Text(
          pages[i].subtitle,
          fontSize = 14.sp,
          color = Color(0xFF9E9EB8),
          textAlign = TextAlign.Center,
          lineHeight = 22.sp
        )
      }
    }

    if (vm.currentPage < pages.size - 1) {
      Row(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          repeat(pages.size) { i ->
            Box(
              Modifier.size(8.dp).clip(CircleShape)
                .background(if (i == vm.currentPage) Color(0xFF7F3DFF) else Color(0xFFE2E8F0))
            )
          }
        }
        Text(
          "Next",
          color = Color(0xFFB0B0C8),
          fontSize = 16.sp,
          modifier = Modifier.clickable {
            scope.launch { pagerState.animateScrollToPage(vm.currentPage + 1) }
          }
        )
      }
    } else {
      Column(Modifier.padding(horizontal = 24.dp)) {
        OutlinedTextField(
          value = vm.name,
          onValueChange = { vm.name = it },
          placeholder = { Text("请告诉我你的名字", color = Color(0xFFB0B0C8)) },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF7F3DFF),
            unfocusedBorderColor = Color(0xFFE2E8F0)
          ),
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(
          onClick = { vm.onGetStarted(onFinished) },
          Modifier.fillMaxWidth().height(56.dp),
          enabled = vm.name.isNotBlank(),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7F3DFF),
            disabledContainerColor = Color(0xFFD1D5DB)
          )
        ) {
          Text(
            if (vm.name.isNotBlank()) "我想好了 >" else "输入内容后继续 >",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
    Spacer(Modifier.height(24.dp))
  }
}
