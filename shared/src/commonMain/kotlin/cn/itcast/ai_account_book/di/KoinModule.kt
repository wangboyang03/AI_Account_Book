package cn.itcast.ai_account_book.di

import cn.itcast.ai_account_book.home.HomeViewModel
import cn.itcast.ai_account_book.transaction.AddTransactionViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule: Module = module {
  factory { HomeViewModel() }
  factory { AddTransactionViewModel() }
}
