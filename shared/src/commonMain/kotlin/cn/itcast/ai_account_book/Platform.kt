package cn.itcast.ai_account_book

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform