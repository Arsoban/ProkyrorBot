package com.arsoban.extension.command

import com.kotlindiscord.kord.extensions.components.components
import com.kotlindiscord.kord.extensions.components.publicButton
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.editingPaginator
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.behavior.edit
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.embed

class HelpExtension : Extension() {
    override val name: String = "HelpExtension"

    override suspend fun setup() {

        publicSlashCommand {

            name = "help"

            description = "Показывает помощь по боту!"

            guild(621722954002333696)

            action {

                respond {

                    embed {

                        title = "Помощь"

                        description = "Выбери снизу категорию для просмотра помощи!"

                        color = Color(0, 255, 0)

                    }

                    components {

                        publicButton(0) {

                            label = "Main"
                            partialEmoji = DiscordPartialEmoji(name = "\uD83D\uDCD6")
                            style = ButtonStyle.Primary
                            id = "mainHelp"

                            action {

                                message!!.edit {

                                    embed {

                                        title = "Помощь по главному модулю"

                                        color = Color(0, 255, 0)

                                        field("/avatar <User>", false) {
                                            "Показывает аватарку юзера"
                                        }

                                    }

                                }

                            }

                        }

                        publicButton(0) {

                            label = "Music"
                            partialEmoji = DiscordPartialEmoji(name = "\uD83C\uDFA7")
                            style = ButtonStyle.Primary
                            id = "musicHelp"

                            action {

                                message!!.edit {

                                    embed {

                                        title = "Помощь по музыкальному модулю"

                                        color = Color(0, 255, 0)

                                        field(",join", false) {
                                            "Присоединяется к голосовому каналу"
                                        }

                                        field(",play <Название трека>", false) {
                                            "Включает трек"
                                        }

                                        field(",skip", false) {
                                            "Пропускает текущий трек"
                                        }

                                        field(",stop", false) {
                                            "Останавливает музыку и очищает очередь"
                                        }

                                        field(",queue", false) {
                                            "Показывает очередь"
                                        }

                                        field(",leave", false) {
                                            "Покидает госоловой канал"
                                        }

                                        field(",repeat", false) {
                                            "Повторяет текущий трек"
                                        }

                                        field(",volume <Громкость>", false) {
                                            "Устанавливает громкость треков"
                                        }

                                        field(",bassboost", false) {
                                            "Включает режим bassboost"
                                        }

                                        field("/lyrics <Название трека>", false) {
                                            "Показывает текст трека"
                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

    }
}