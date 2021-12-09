package com.arsoban.extension.command

import com.arsoban.com.arsoban.serializer.LyricsSerializer
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import io.ktor.client.*
import io.ktor.client.request.*
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class LyricsExtension : Extension() {

    private val client: HttpClient by inject(named("httpClient"))

    class LyricsArgs : Arguments() {
        val track by string("track-name", "Название трека")
    }

    override val name: String = "LyricsExtension"

    override suspend fun setup() {
        publicSlashCommand(LyricsExtension::LyricsArgs) {
            name = "lyrics"
            description = "Показывает текст указанного трека"

            guild(621722954002333696)

            action {

                try {
                    val track: LyricsSerializer = client.get("https://some-random-api.ml/lyrics"){
                        parameter("title", arguments.track)
                    }

                    respond {
                        content = "Название трека: ${track.title}\n" +
                                "Автор: ${track.author}\n" +
                                "Текст: ${track.lyrics}"
                    }
                } catch (exc: Exception){
                    respond {
                        content = ":x: Ошибка, я не нашёл такого трека"
                    }
                }

            }
        }
    }
}