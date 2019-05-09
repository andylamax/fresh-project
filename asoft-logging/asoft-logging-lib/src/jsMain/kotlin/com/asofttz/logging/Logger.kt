package com.asofttz.logging

actual class Logger actual constructor(actual val source: String, actual val server: LogServer?) {

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        console.log("$log")
        server?.pushToServer(log)
    }

    actual fun e(msg: String) {
        val log = Log(Log.Level.ERROR, msg, source)
        console.error("$log")
        server?.pushToServer(log)
    }

    actual fun f(msg: String) {
        val log = Log(Log.Level.FAILURE, msg, source)
        console.error("$log")
        server?.pushToServer(log)
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        console.warn("$log")
        server?.pushToServer(log)
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
        console.info("$log")
        server?.pushToServer(log)
    }

    actual fun obj(vararg o: Any?) {
        console.log(o)
    }

    actual fun obj(o: Any?) = console.log(o)
}