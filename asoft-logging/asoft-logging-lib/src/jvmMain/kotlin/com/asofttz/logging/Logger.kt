package com.asofttz.logging

actual class Logger actual constructor(actual val source: String, actual val server: LogServer?) {

    actual fun d(msg: String) {
        val log = Log(Log.Level.DEBUG, msg, source)
        Color.Blue.println(log)
        server?.pushToServer(log)
    }

    actual fun e(msg: String) {
        val log = Log(Log.Level.ERROR, msg, source)
        Color.Red.println(log)
        server?.pushToServer(log)
    }

    actual fun f(msg: String) {
        val log = Log(Log.Level.FAILURE, msg, source)
        Color.Red.println(log)
        server?.pushToServer(log)
    }

    actual fun w(msg: String) {
        val log = Log(Log.Level.WARNING, msg, source)
        Color.Yellow.println(log)
        server?.pushToServer(log)
    }

    actual fun i(msg: String) {
        val log = Log(Log.Level.INFO, msg, source)
        Color.Normal.println(log)
        server?.pushToServer(log)
    }

    actual fun obj(vararg o: Any?) = o.forEach {
        val log = Log(Log.Level.INFO, it.toString(), source)
        Color.Normal.println(log)
    }

    actual fun obj(o: Any?) {
        val log = Log(Log.Level.INFO, o.toString(), source)
        Color.Normal.println(log)
    }

    enum class Color(private val escape: String) {
        Red("\u001B[31m"),
        Maroon("\u001B[35m"),
        Yellow("\u001B[33m"),
        Blue("\u001B[36m"),
        Normal("\u001B[0m");

        fun println(log: Log) {
            println("$escape$log\u001B[0m")
        }
    }
}
