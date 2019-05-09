package com.asofttz.logging

import com.asofttz.logging.data.viewmodal.LogViewModal
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class LogServer(private val logViewModal: LogViewModal) {

    fun getLogger(source: String) = Logger(source, this)

    fun pushToServer(log: Log) = GlobalScope.launch { logViewModal.saveLog(log) }
}