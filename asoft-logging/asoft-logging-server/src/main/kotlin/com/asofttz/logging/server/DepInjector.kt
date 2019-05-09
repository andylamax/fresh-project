package com.asofttz.logging.server

import com.asofttz.logging.Logger
import com.asofttz.logging.data.dao.ServerLogDao
import com.asofttz.logging.data.viewmodal.LogViewModal
import com.asofttz.persist.DataSourceConfig
import com.asofttz.persist.RepoFactory

object injection {

    val logger = Logger("logging-server")

    val config = DataSourceConfig("bolt://logging-db:7687")

    init {
        val classLoader = javaClass.classLoader
        try {
            val file = classLoader.getResourceAsStream("credentials.txt")

            println("\n\n\n\n")
            logger.i("Loading Credentials")

            val line = file.reader().readLines()
            config.apply {
                try {
                    username = line[0].split(" ")[0]
                    password = line[0].split(" ")[1]
                    logger.i("Success")
                } catch (e: Exception) {
                    logger.f("Failed getting credentials from file: $e")
                }
            }
        } catch (e: Exception) {
            println("\n\n\n\n")
            logger.e("Couldn't find credentials.txt file\n\n\n\n")
            throw Exception("No Credentials File")
        }
    }

    private val logDao = ServerLogDao.getInstance(config)

    private val logRepo = RepoFactory.getRepo(logDao)

    val viewModal: LogViewModal
        get() = LogViewModal(logRepo)
}