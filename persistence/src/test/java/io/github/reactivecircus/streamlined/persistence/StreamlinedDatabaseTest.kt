package io.github.reactivecircus.streamlined.persistence

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

class StreamlinedDatabaseTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        StreamlinedDatabase.Schema.create(this)
    }

    private val queries = StreamlinedDatabase(inMemorySqlDriver).storyEntityQueries
}
