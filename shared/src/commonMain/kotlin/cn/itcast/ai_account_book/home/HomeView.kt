package cn.itcast.ai_account_book.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.itcast.ai_account_book.data.UserStore
import cn.itcast.ai_account_book.transaction.TransactionItem
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun HomeTabPage(vm: HomeViewModel, onEditTransaction: (TransactionItem) -> Unit = {}) {
  val state by vm.uiState.collectAsState()
  val primary = Color(0xFF7F3DFF)

  Column(
    Modifier.fillMaxSize()
      .background(Brush.verticalGradient(listOf(Color.White, Color(0xFFEFF1F5))))
  ) {
    Text(
      "${state.userName}，欢迎回来",
      fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23233C),
      modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 4.dp)
    )
    Text(
      state.poem, fontSize = 12.sp, color = Color(0xFF9E9EB8),
      modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
    )

    Row(
      Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      state.cards.forEachIndexed { i, card ->
        Card(
          Modifier.width(140.dp).height(85.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = if (i == 0) primary else Color.White),
          elevation = CardDefaults.cardElevation(4.dp)
        ) {
          Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text(card.label, fontSize = 11.sp,
              color = if (i == 0) Color(0xFFE0D6FF) else Color(0xFF9E9EB8))
            Text(card.display, fontSize = 18.sp, fontWeight = FontWeight.Bold,
              color = if (i == 0) Color.White else Color(0xFF23233C))
          }
        }
      }
    }

    Spacer(Modifier.height(20.dp))

    Row(
      Modifier.fillMaxWidth().padding(horizontal = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
      Text("最近记录", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23233C))
    }
    Spacer(Modifier.height(12.dp))

    if (state.transactions.isEmpty()) {
      Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
        Text("暂无记录，点击 + 开始记账", fontSize = 14.sp, color = Color(0xFFB0B0C8))
      }
    } else {
      LazyColumn(
        Modifier.weight(1f).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(state.transactions.size, key = { state.transactions[it].id }) { i ->
          val tx = state.transactions[i]
          TransactionRow(tx, { vm.deleteTransaction(tx.id) }, { onEditTransaction(tx) })
        }
      }
    }
  }
}

@Composable
private fun TransactionRow(
  tx: TransactionItem,
  onDelete: () -> Unit,
  onEdit: () -> Unit
) {
  val isExpense = tx.type == "expense"
  var showMenu by remember { mutableStateOf(false) }

  Card(
    Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Box {
      Row(
        Modifier.padding(14.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
      ) {
        Column(Modifier.weight(1f)) {
          Text(tx.category, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF23233C))
          Text(formatDateTime(tx.date), fontSize = 11.sp, color = Color(0xFF9E9EB8))
          if (tx.note.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(tx.note, fontSize = 12.sp, color = Color(0xFF6B6B80), maxLines = 2)
          }
        }
        Text(
          "${if (isExpense) "-" else "+"}¥${"%.2f".format(tx.amount)}",
          fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
          color = if (isExpense) Color(0xFFE53E3E) else Color(0xFF38A169)
        )
      }

      // Long-press menu
      DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
      ) {
        DropdownMenuItem(
          text = { Text("修改") },
          onClick = { showMenu = false; onEdit() }
        )
        DropdownMenuItem(
          text = { Text("删除", color = Color(0xFFE53E3E)) },
          onClick = { showMenu = false; onDelete() }
        )
      }

      // Invisible overlay for long-press and right-click detection
      Box(
        Modifier.fillMaxSize()
          .pointerInput(tx.id) {
            detectTapGestures(
              onLongPress = { showMenu = true }
            )
          }
          .pointerInput(tx.id) {
            awaitPointerEventScope {
              while (true) {
                val event = awaitPointerEvent()
                if (event.type == PointerEventType.Press) {
                  if (event.buttons.isSecondaryPressed) {
                    showMenu = true
                  }
                }
              }
            }
          }
      )
    }
  }
}

// ── Chart Page ──

@Composable
fun ChartPage(vm: HomeViewModel) {
  val state by vm.uiState.collectAsState()
  val textMeasurer = rememberTextMeasurer()

  Column(
    Modifier.fillMaxSize().background(Color.White).safeContentPadding().padding(horizontal = 24.dp)
  ) {
    Text("图表分析", fontSize = 20.sp, fontWeight = FontWeight.Bold,
      color = Color(0xFF23233C), modifier = Modifier.padding(top = 16.dp, bottom = 20.dp))
    Text("支出分类", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF23233C))
    Spacer(Modifier.height(12.dp))
    PieChart(state.transactions)
    Spacer(Modifier.height(24.dp))
    Text("收支趋势", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF23233C))
    Spacer(Modifier.height(12.dp))
    LineChart(state.transactions, textMeasurer)
  }
}

@Composable
private fun PieChart(transactions: List<TransactionItem>) {
  val expenses = transactions.filter { it.type == "expense" }
  val grouped = expenses.groupBy { it.category }.map { (cat, list) -> cat to list.sumOf { it.amount } }
    .sortedByDescending { it.second }
  val total = grouped.sumOf { it.second }
  val colors = listOf(
    Color(0xFF7F3DFF), Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFFFF8E53),
    Color(0xFF60A5FA), Color(0xFFA78BFA), Color(0xFF34D399), Color(0xFFF59E0B)
  )
  if (total == 0.0) {
    Box(Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
      Text("暂无支出数据", color = Color(0xFFB0B0C8), fontSize = 14.sp)
    }
    return
  }
  Row(Modifier.fillMaxWidth().height(200.dp)) {
    Canvas(Modifier.size(160.dp)) {
      var startAngle = -90f
      grouped.forEachIndexed { i, (_, amount) ->
        val sweep = (amount / total * 360).toFloat()
        drawArc(colors[i % colors.size], startAngle, sweep, true,
          topLeft = Offset(16f, 16f), size = Size(size.minDimension - 32f, size.minDimension - 32f))
        startAngle += sweep
      }
    }
    Column(Modifier.weight(1f).padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      grouped.take(6).forEachIndexed { i, (cat, amount) ->
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(Modifier.size(10.dp).background(colors[i % colors.size], CircleShape))
          Spacer(Modifier.width(6.dp))
          Text("$cat ${"%.0f".format(amount / total * 100)}%", fontSize = 11.sp, color = Color(0xFF23233C))
        }
      }
    }
  }
}

@Composable
private fun LineChart(transactions: List<TransactionItem>, textMeasurer: androidx.compose.ui.text.TextMeasurer) {
  val daily = transactions.groupBy { tx ->
    val local = Instant.fromEpochMilliseconds(tx.date).toLocalDateTime(TimeZone.currentSystemDefault())
    "${local.monthNumber}/${local.dayOfMonth}"
  }.map { (d, list) -> d to list.sumOf { if (it.type == "expense") -it.amount else it.amount } }.takeLast(7)

  if (daily.isEmpty()) {
    Box(Modifier.height(180.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
      Text("暂无数据", color = Color(0xFFB0B0C8), fontSize = 14.sp)
    }
    return
  }
  val primary = Color(0xFF7F3DFF)
  val values = daily.map { it.second }
  val maxV = (values.maxOrNull() ?: 0.0).coerceAtLeast(1.0)
  val minV = (values.minOrNull() ?: 0.0).coerceAtMost(0.0)
  val range = (maxV - minV).coerceAtLeast(1.0)

  Canvas(Modifier.fillMaxWidth().height(180.dp).padding(top = 8.dp)) {
    val padL = 36f; val padR = 12f; val padT = 12f; val padB = 28f
    val cw = size.width - padL - padR; val ch = size.height - padT - padB
    for (i in 0..3) {
      val y = padT + ch * i / 3f
      drawLine(Color(0xFFE8E8F0), Offset(padL, y), Offset(padL + cw, y), strokeWidth = 0.5f)
    }
    if (daily.size >= 2) {
      val points = daily.mapIndexed { i, (_, v) ->
        Offset(padL + cw * i / (daily.size - 1), padT + ch - ((v - minV) / range * ch).toFloat())
      }
      val fill = Path().apply {
        moveTo(points.first().x, padT + ch)
        points.forEach { lineTo(it.x, it.y) }
        lineTo(points.last().x, padT + ch); close()
      }
      drawPath(fill, primary.copy(alpha = 0.08f), style = Fill)
      for (i in 0 until points.size - 1) drawLine(primary, points[i], points[i + 1], strokeWidth = 2f)
      points.forEach { drawCircle(primary, 3.5f, it) }
    }
    val labelStyle = TextStyle(fontSize = 9.sp, color = Color(0xFF9E9EB8))
    daily.forEachIndexed { i, (d, _) ->
      val x = if (daily.size == 1) padL + cw / 2 else padL + cw * i / (daily.size - 1)
      drawText(textMeasurer.measure(d, labelStyle), topLeft = Offset(x - 14f, padT + ch + 6f))
    }
  }
}

// ── Settings Page ──

@Composable
fun SettingsPage() {
  var name by mutableStateOf("")
  var saved by mutableStateOf(false)
  val scope = rememberCoroutineScope()
  val primary = Color(0xFF7F3DFF)

  LaunchedEffect(Unit) { name = UserStore.getUserName() ?: "" }

  Column(Modifier.fillMaxSize().background(Color.White).safeContentPadding().padding(24.dp)) {
    Text("设置", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23233C))
    Spacer(Modifier.height(24.dp))
    Text("用户名", fontSize = 14.sp, color = Color(0xFF9E9EB8))
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
      value = name, onValueChange = { name = it; saved = false },
      modifier = Modifier.fillMaxWidth(),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primary, unfocusedBorderColor = Color(0xFFE8E8F0), cursorColor = primary
      )
    )
    Spacer(Modifier.height(12.dp))
    Button(
      onClick = { scope.launch { UserStore.saveUserName(name.trim()); saved = true } },
      colors = ButtonDefaults.buttonColors(containerColor = primary),
      shape = RoundedCornerShape(10.dp)
    ) { Text("保存", color = Color.White) }
    if (saved) {
      Spacer(Modifier.height(8.dp))
      Text("已保存", fontSize = 12.sp, color = Color(0xFF38A169))
    }
  }
}

// ─ Helpers ──

private fun formatDateTime(epochMs: Long): String {
  val local = Instant.fromEpochMilliseconds(epochMs).toLocalDateTime(TimeZone.currentSystemDefault())
  val date = "${local.year}-${local.monthNumber.toString().padStart(2, '0')}-${local.dayOfMonth.toString().padStart(2, '0')}"
  val time = "${local.hour.toString().padStart(2, '0')}:${local.minute.toString().padStart(2, '0')}:${local.second.toString().padStart(2, '0')}"
  return "$date $time"
}
