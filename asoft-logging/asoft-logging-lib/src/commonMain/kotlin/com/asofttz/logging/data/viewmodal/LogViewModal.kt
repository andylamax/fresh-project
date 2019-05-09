package com.asofttz.logging.data.viewmodal

import com.asofttz.logging.Log
import com.asofttz.persist.Repo

class LogViewModal(private val repo: Repo<Log>) {

    suspend fun filter(predicate: (Log) -> Boolean) = repo.filter(predicate)

    suspend fun getLogs() = repo.loadAll()

    suspend fun saveLog(log: Log) = repo.create(log)
}