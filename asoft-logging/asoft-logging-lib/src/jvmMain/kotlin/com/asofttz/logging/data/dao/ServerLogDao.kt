package com.asofttz.logging.data.dao

import com.asofttz.logging.Log
import com.asofttz.persist.Dao
import com.asofttz.persist.DataSourceConfig
import com.asofttz.rx.ObservableList
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

class ServerLogDao private constructor(config: DataSourceConfig) : Dao<Log>() {
    companion object {
        private var instance: Dao<Log>? = null
        fun getInstance(config: DataSourceConfig): Dao<Log> {
            synchronized(this) {
                if (instance == null) {
                    instance = ServerLogDao(config)
                }
                return instance!!
            }
        }
    }

    private val configuration = Configuration.Builder()
            .uri(config.url)
            .credentials(config.username, config.password)
            .build()

    private val sessionFactory = SessionFactory(configuration,
            Log::class.java.`package`.name
    )

    override suspend fun create(log: Log): Boolean {
        synchronized(this) {
            with(sessionFactory.openSession()) {
                save(log)
                cached.add(0, log)
                clear()
            }
        }
        return true
    }

    override suspend fun edit(t: Log): Boolean {
        return false
    }

    override suspend fun delete(t: Log): Boolean {
        return false
    }

    override suspend fun load(id: Int): Log? {
        return cached.value.getOrNull(id)
    }

    override suspend fun loadAll(): ObservableList<Log> {
        if (cached.size == 0) {
            with(sessionFactory.openSession()) {
                cached.value = loadAll(Log::class.java).toMutableList()
            }
        }
        return cached
    }
}