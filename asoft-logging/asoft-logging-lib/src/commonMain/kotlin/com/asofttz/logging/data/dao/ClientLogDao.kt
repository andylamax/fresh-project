package com.asofttz.logging.data.dao

import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import com.asofttz.logging.data.db.LogDataSourceConfig
import com.asofttz.persist.Dao
import com.asofttz.persist.DataSourceConfig
import com.asofttz.rx.ObservableList
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.HttpEngineCall
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlin.coroutines.CoroutineContext

class ClientLogDao private constructor(private val config: DataSourceConfig) : Dao<Log>() {

    private val logger = Logger("log-engine")

    private val client = HttpClient {
        expectSuccess = false
    }

    companion object {
        private var instance: Dao<Log>? = null
        fun getInstance(config: DataSourceConfig): Dao<Log> {
            if (instance == null) {
                instance = ClientLogDao(config)
            }
            return instance!!
        }
    }

    override suspend fun create(log: Log): Boolean {
        cached.add(0, log)
        val url = "${config.url}/log"
        val logJson = JSON.stringify(Log.serializer(), log)
        try {
            val response = client.post<HttpResponse>(url) {
                body = TextContent(logJson, ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                logger.w("Failed to send log {${log.msg}} to server")
                return false
            }
        } catch (e: Throwable) {
            logger.e("Failed to send log \"${log.msg}\" to server")
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
            try {
                val url = "${config.url}/logs"
                val response = client.get<HttpResponse>(url)
                if (response.status == HttpStatusCode.OK) {
                    val jsonLogs = response.readText()
                    val logs = JSON.parse(Log.serializer().list, jsonLogs)
                    cached.value = logs as MutableList<Log>
                } else {
                    logger.e("Couldn't retrieve logs from server")
                }
            } catch (e: Throwable) {
                logger.e(e.message.toString())
            }
        }
        return cached
    }
}