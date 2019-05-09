package com.asofttz.logging.server

import com.asofttz.logging.Log
import com.asofttz.logging.Logger
import com.asofttz.logging.data.viewmodal.LogViewModal
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.CORS
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.stringify
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {
    install(CORS) {
        anyHost()
    }

    val logger = injection.logger

    routing {
        get("/logs") {
            val viewModal = injection.viewModal
            try {
                val logs = viewModal.getLogs().value.asReversed()
                val logsJson = JSON.indented.stringify(Log.serializer().list, logs)
                logger.i("Sending logs")
                call.respondJson(logsJson)
            } catch (e: Throwable) {
                call.respondJson("""{
                    "status": false,
                    "cause": "${e.message}"
                    }""".trimMargin())
            }
        }

        post("/log") {
            val viewModal = injection.viewModal
            val log = JSON.parse(Log.serializer(), call.receive())
            log.log()
            viewModal.saveLog(log)
            call.respond(HttpStatusCode.OK)
        }
    }
}

suspend fun ApplicationCall.respondJson(json: String) = respondText(json, ContentType.Application.Json)