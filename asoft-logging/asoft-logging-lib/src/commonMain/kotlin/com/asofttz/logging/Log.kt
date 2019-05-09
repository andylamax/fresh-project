package com.asofttz.logging

import com.asofttz.date.Date
import com.asofttz.date.DateFactory
import com.asofttz.date.DateSerializer
import com.asofttz.date.string
import com.asofttz.logging.neo4j.GeneratedValue
import com.asofttz.logging.neo4j.Id
import com.asofttz.logging.neo4j.NodeEntity
import com.asofttz.logging.neo4j.Property
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@NodeEntity
open class Log(
        @Property var level: Level = Level.DEBUG,
        @Property var msg: String = "",
        @Property var source: String = "anonymous"
) {

    @Id
    @GeneratedValue
    var id: Long? = null

    @Serializable(with = DateSerializer::class)
    @Property
    var time: Date = DateFactory.today()

    @Transient
    private val logger: Logger
        get() = Logger(source)

    enum class Level {
        ERROR, WARNING, DEBUG, FAILURE, INFO
    }

    override fun toString() = "${time.string()} [$level] $source - $msg"

    fun log() = when (level) {
        Level.ERROR -> logger.e(msg)
        Level.WARNING -> logger.w(msg)
        Level.DEBUG -> logger.d(msg)
        Level.FAILURE -> logger.f(msg)
        Level.INFO -> logger.i(msg)
    }
}

