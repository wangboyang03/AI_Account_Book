package cn.itcast.ai_account_book.transaction

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import ai_account_book.shared.generated.resources.*

data class TransactionItem(
  val id: Long,
  val amount: Double,
  val type: String,
  val category: String,
  val note: String,
  val date: Long,
  val createdAt: Long
)

data class CategoryInfo(
  val name: String,
  val icon: DrawableResource,
  val color: Color
)

val expenseCategories = listOf(
  CategoryInfo("餐饮", Res.drawable.icon_homepage_fastFood, Color(0xFFFF6B6B)),
  CategoryInfo("交通", Res.drawable.icon_homepage_travel, Color(0xFF4ECDC4)),
  CategoryInfo("购物", Res.drawable.icon_homepage_shopping, Color(0xFFFF8E53)),
  CategoryInfo("娱乐", Res.drawable.icon_homepage_film, Color(0xFFA78BFA)),
  CategoryInfo("住房", Res.drawable.icon_homepage_rent, Color(0xFF60A5FA)),
  CategoryInfo("通讯", Res.drawable.icon_homapage_vector, Color(0xFF34D399))
)

val incomeCategories = listOf(
  CategoryInfo("工资", Res.drawable.icon_homepage_money, Color(0xFF7F3DFF)),
  CategoryInfo("退款", Res.drawable.icon_homepage_cashback, Color(0xFF4ECDC4)),
  CategoryInfo("副业", Res.drawable.icon_homepage_architecture, Color(0xFFFF8E53))
)
