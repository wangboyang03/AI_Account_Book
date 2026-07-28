package cn.itcast.ai_account_book.onboarding

import ai_account_book.shared.generated.resources.Res
import ai_account_book.shared.generated.resources.icon_splash_coin
import ai_account_book.shared.generated.resources.icon_splash_record
import ai_account_book.shared.generated.resources.icon_splash_statistic
import org.jetbrains.compose.resources.DrawableResource

data class OnboardingPage(
  val icon: DrawableResource,
  val title: String,
  val subtitle: String
)

val pages = listOf(
  OnboardingPage(
    Res.drawable.icon_splash_coin,
    "随手记开销",
    "每日记录收支，轻松打理个人财务"
  ),
  OnboardingPage(
    Res.drawable.icon_splash_record,
    "简单高效理财",
    "一旦支出超标，及时推送超支预警"
  ),
  OnboardingPage(
    Res.drawable.icon_splash_statistic,
    "收支数据一目了然",
    "全程追踪消费明细，理性消费不月光"
  )
)
