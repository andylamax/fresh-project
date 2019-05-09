package com.asofttz.logging

expect class Logger(source: String = "anonymous", server: LogServer? = null) {
    val source: String
    val server: LogServer?
    fun d(msg: String)
    fun e(msg: String)
    fun f(msg: String)
    fun w(msg: String)
    fun i(msg: String)
    fun obj(vararg o: Any?)
    fun obj(o: Any?)
}