package com.arsoban.prokyrorapi

import com.arsoban.prokyrorapi.serializer.ReportSerializer
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.rest.builder.message.create.embed
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.GlobalContext

suspend fun startServer(port: Int) {

    embeddedServer(Netty, port = port) {

        install(ContentNegotiation) {
            json()
        }

        routing {

            post("/sendReport") {

                try {

                    val report = call.receive<ReportSerializer>()

                    val kord = GlobalContext.get().inject<Kord>().value

                    val message = kord.rest.channel.createMessage(Snowflake(764430220035883008)) {

                        embed {

                            title = "Поступила новая жалоба через rest api!"

                            color = Color(255, 0, 0)

                            description = """
Пожаловался: ${report.reporter}
Пожаловался на: ${report.userReported}
Жалоба: ${report.report}
                        """

                        }

                    }

                } catch (exc: Exception) {
                    exc.printStackTrace()
                    call.respondText("""
{
  "result": "error"
}
                """)
                    return@post
                }

                call.respondText("""
{
  "result": "success"
}
                """)

            }

        }

    }.start(wait = true)

}