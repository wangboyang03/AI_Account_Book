package cn.itcast.ai_account_book

import kotlin.test.Test
import kotlin.test.assertEquals

class SharedLogicDesktopTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @Test
    fun databaseMigrationAddsUserNameColumn() {
        // Accessing Database.db triggers the idempotent bootstrap/migration,
        // adding the user_name column to any pre-existing database.
        val db = cn.itcast.ai_account_book.db.Database.db
        // Querying by user_name proves the column exists (would throw otherwise).
        val rows = db.transactionQueries.selectAll("__no_such_user__").executeAsList()
        assertEquals(0, rows.size)
    }
}