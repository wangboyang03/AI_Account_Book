package cn.itcast.ai_account_book.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
  editTransaction: TransactionItem? = null,
  onBack: () -> Unit,
  onSaved: () -> Unit = {}
) {
  val vm = remember { AddTransactionViewModel(editTransaction) }
  val primary = Color(0xFF7F3DFF)
  val isEditing = editTransaction != null

  Column(Modifier.fillMaxSize().background(Color.White).safeContentPadding()) {
    // Header
    Row(
      Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text("取消", color = Color(0xFF9E9EB8), fontSize = 14.sp,
        modifier = Modifier.clickable { onBack() })
      Text(if (isEditing) "修改" else "记一笔", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF23233C))
      Text("保存", color = primary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clickable { vm.save { onSaved(); onBack() } })
    }

    // Type tabs
    Row(Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      listOf("expense" to "支出", "income" to "收入").forEach { (t, label) ->
        val selected = vm.type == t
        Text(
          label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
          color = if (selected) Color.White else Color(0xFF9E9EB8),
          textAlign = TextAlign.Center,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) primary else Color(0xFFF0F0F5))
            .clickable { vm.updateType(t) }
            .padding(horizontal = 24.dp, vertical = 8.dp)
        )
      }
    }

    Spacer(Modifier.height(20.dp))

    // Category grid
    val cats = vm.categories
    Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      cats.chunked(3).forEach { row ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          row.forEachIndexed { i, cat ->
            CategoryItem(cat, vm.selectedCategory == cats.indexOf(cat), primary,
              Modifier.weight(1f).clickable { vm.updateCategory(cats.indexOf(cat)) })
          }
          repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
        }
      }
    }

    Spacer(Modifier.height(24.dp))

    // Amount
    Column(Modifier.padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text("金额", fontSize = 12.sp, color = Color(0xFF9E9EB8))
      Spacer(Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("¥", fontSize = 24.sp, color = Color(0xFF23233C), fontWeight = FontWeight.Bold)
        OutlinedTextField(
          value = vm.amount,
          onValueChange = { v -> vm.updateAmount(v.filter { it.isDigit() || it == '.' }) },
          modifier = Modifier.widthIn(min = 150.dp).padding(start = 8.dp),
          textStyle = LocalTextStyle.current.copy(
            fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            color = Color(0xFF23233C)
          ),
          placeholder = { Text("0.00", fontSize = 36.sp, color = Color(0xFFD0D0D0), textAlign = TextAlign.Center) },
          singleLine = true,
          keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent,
            cursorColor = primary
          )
        )
      }
    }

    Spacer(Modifier.height(16.dp))

    // Note
    OutlinedTextField(
      value = vm.note, onValueChange = { vm.updateNote(it) },
      modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
      placeholder = { Text("添加备注（可选）", color = Color(0xFFB0B0C8), fontSize = 14.sp) },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = primary, unfocusedBorderColor = Color(0xFFE8E8F0),
        cursorColor = primary
      )
    )

    Spacer(Modifier.weight(1f))

    // Save button
    Button(
      onClick = { vm.save { onSaved(); onBack() } },
      modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 20.dp),
      colors = ButtonDefaults.buttonColors(containerColor = primary),
      shape = RoundedCornerShape(12.dp),
      enabled = vm.amount.toDoubleOrNull() != null && vm.amount.toDouble() > 0
    ) {
      Text("保存", fontSize = 16.sp, color = Color.White)
    }
    Spacer(Modifier.height(16.dp))
  }
}

@Composable
private fun CategoryItem(cat: CategoryInfo, selected: Boolean, primary: Color, modifier: Modifier) {
  Column(
    modifier.then(if (selected) Modifier else Modifier),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      Modifier.size(48.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(if (selected) primary.copy(alpha = 0.15f) else Color(0xFFF5F5FA)),
      contentAlignment = Alignment.Center
    ) {
      androidx.compose.foundation.Image(
        painter = painterResource(cat.icon),
        contentDescription = cat.name,
        modifier = Modifier.size(24.dp)
      )
    }
    Spacer(Modifier.height(4.dp))
    Text(cat.name, fontSize = 11.sp,
      color = if (selected) primary else Color(0xFF9E9EB8),
      fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
  }
}
