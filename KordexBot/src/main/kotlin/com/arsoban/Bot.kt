package com.arsoban

import com.arsoban.extension.context.ReportExtension
import com.arsoban.extension.command.AvatarExtension
import com.arsoban.extension.command.LyricsExtension
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

class Bot {

    suspend fun createBot(token: String) {
        val bot = ExtensibleBot(token) {

            extensions {
                add(::ReportExtension)
                add(::AvatarExtension)
                add(::LyricsExtension)
            }

            presence {
                watching("На Дурку")
            }

            intents {

            }

        }

        val client: HttpClient = HttpClient(CIO){
            install(JsonFeature){
                serializer = KotlinxSerializer()
            }

            install(Logging){
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }

        val mainModule = module {
            single(named("httpClient")) { client }
        }

        bot.getKoin().loadModules(listOf(mainModule))

        bot.start()
    }

}
