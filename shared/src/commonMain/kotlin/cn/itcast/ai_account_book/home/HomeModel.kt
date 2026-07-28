package cn.itcast.ai_account_book.home

data class OverviewCard(
  val label: String,
  val amount: Double
)

data class Transaction(
  val id: String,
  val category: String,
  val date: String,
  val amount: Double,
  val vat: Double,
  val method: String
)

data class HomeUiState(
  val userName: String = "",
  val poem: String = "",
  val cards: List<OverviewCard> = emptyList(),
  val transactions: List<Transaction> = emptyList()
)

val poems = listOf(
  "此情可待成追忆，只是当时已惘然。",
  "离别家乡岁月多，近来人事半消磨。",
  "人面不知何处去，桃花依旧笑春风。",
  "被酒莫惊春睡重，赌书消得泼茶香，当时只道是寻常。",
  "若教眼底无离恨，不信人间有白头。肠已断，泪难收。相思重上小红楼。",
  "侯门一入深如海，从此萧郎是路人。",
  "花开堪折直须折，莫待无花空折枝。",
  "还君明珠双泪垂，恨不相逢未嫁时。",
  "死去元知万事空，但悲不见九州同。",
  "当年不肯嫁春风，无端却被秋风误。",
  "浮云蔽白日，游子不顾返。思君令人老，岁月忽已晚。",
  "年年岁岁花相似，岁岁年年人不同。",
  "了却君王天下事，赢得生前身后名。可怜白发生。",
  "无可奈何花落去，似曾相识燕归来。小园香径独徘徊。",
  "似此星辰非昨夜，为谁风露立中宵。",
  "世间无限丹青手，一片伤心画不成。",
  "物是人非事事休，欲语泪先流。",
  "悲欢离合总无情。一任阶前、点滴到天明。",
  "自是寻春去校迟，不须惆怅怨芳时。",
  "多少蓬莱旧事，空回首、烟霭纷纷。斜阳外，寒鸦万点，流水绕孤村。"
)
