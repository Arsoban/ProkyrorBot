package com.arsoban.extension.context

import com.kotlindiscord.kord.extensions.checks.guildFor
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralMessageCommand
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.rest.builder.message.create.actionRow
import dev.kord.rest.builder.message.create.embed
import dev.kord.rest.builder.message.modify.actionRow

class ReportExtension : Extension() {
    override val name: String = "ReportExtension"

    override suspend fun setup() {
        ephemeralMessageCommand {
            name = "Жалоба"

            guild(621722954002333696)

            action {
                try {
                    if (!targetMessages.first().author!!.asUser().isBot) {

                        if (!event.interaction.user.asMember(guild!!.id).roleIds.contains(Snowflake(913316623098716210))){
                            event.kord.rest.channel.createMessage(Snowflake(764430220035883008)) {

                                embed {
                                    title = "Жалоба на сообщение!"

                                    description = """
                            Сообщение отправил: `${targetMessages.first().author!!.username}#${targetMessages.first().author!!.discriminator}`
                            ID автора сообщения: `${targetMessages.first().author!!.id.value}`
                            Канал с сообщением: ${channel.mention}
                            Ссылка на сообщение: https://discord.com/channels/${targetMessages.first().getGuild().id.value}/${targetMessages.first().channel.id.value}/${targetMessages.first().id.value}
                            Контент сообщения: ```${targetMessages.first().content}```
                            Отправил репорт: `${user.asUser().username}#${user.asUser().discriminator}`
                        """

                                    if (targetMessages.first().attachments.isNotEmpty()){
                                        image = targetMessages.first().attachments.first().url
                                    }

                                    color = Color(255, 0, 0)

                                    footer {
                                        text = "${user.asUser().username}#${user.asUser().discriminator}"
                                        icon = user.asUser().avatar!!.url
                                    }
                                }

                                actionRow {
//                                linkButton("https://discord.com/channels/${targetMessages.first().getGuild().id.value}/${targetMessages.first().channel.id.value}/${targetMessages.first().id.value}"){
//                                    label = "Перейти"
//                                    emoji = DiscordPartialEmoji(name = "\uD83D\uDD17")
//                                }

                                    if (!event.interaction.user.asMember(guild!!.id).roleIds.contains(Snowflake(913316623098716210))) {
                                        interactionButton(ButtonStyle.Danger, "deny${event.interaction.user.id}"){
                                            label = "Запрет"
                                            emoji = DiscordPartialEmoji(name = "❌")
                                        }
                                    } else {
                                        interactionButton(ButtonStyle.Success, "deny${event.interaction.user.id}"){
                                            label = "Запрет"
                                            emoji = DiscordPartialEmoji(name = "✅")
                                        }
                                    }

                                    interactionButton(ButtonStyle.Danger, "close"){
                                        label = "Закрыть"
                                        emoji = DiscordPartialEmoji(name = "\uD83D\uDDC4")
                                    }
                                }

                            }

                            respond {
                                ephemeral = true

                                content = ":white_check_mark: Жалоба отправлена!"
                            }
                        } else {
                            respond {
                                ephemeral = true

                                content = ":x: Тебе выдали запрет на жалобы!"
                            }
                        }
                    } else {
                        respond {
                            ephemeral = true

                            content = ":x: Ты не можешь кинуть репорт на сообщение от бота!"
                        }
                    }
                } catch (exc: Exception){
                    exc.printStackTrace()

                    respond {
                        ephemeral = true

                        content = ":x: Ошибка :("
                    }
                }
            }
        }

        event<ButtonInteractionCreateEvent> {
            action {
                if (event.interaction.componentId.startsWith("deny")){

                    if (event.interaction.user.asMember(guildFor(event)!!.id).roleIds.contains(Snowflake(695711294061412463))) {

                        val memberId = event.interaction.componentId.replace("deny", "").replace("Snowflake(value=", "").replace(")", "")

                        val member = event.kord.getUser(Snowflake(memberId.toLong()))!!.asMember(guildFor(event)!!.id)

                        if (!member.roleIds.contains(Snowflake(913316623098716210))){
                            member.addRole(Snowflake(913316623098716210), "Запрет на жалобы от модератора ${event.interaction.user.username}#${event.interaction.user.discriminator}")

                            event.interaction.message!!.edit {
                                actionRow {
                                    interactionButton(ButtonStyle.Success, "deny${event.interaction.user.id}"){
                                        label = "Запрет"
                                        emoji = DiscordPartialEmoji(name = "✅")
                                    }

                                    interactionButton(ButtonStyle.Danger, "close"){
                                        label = "Закрыть"
                                        emoji = DiscordPartialEmoji(name = "\uD83D\uDDC4")
                                    }
                                }
                            }

                            event.interaction.respondPublic {
                                content = "Модератор ${event.interaction.user.username}#${event.interaction.user.discriminator} выдал запрет на создание жалоб юзеру ${member.username}#${member.discriminator}!"
                            }

                        } else {
                            member.removeRole(Snowflake(913316623098716210), "Снятие запрета на жалобы от модератора ${event.interaction.user.username}#${event.interaction.user.discriminator}")

                            event.interaction.message!!.edit {
                                actionRow {
                                    interactionButton(ButtonStyle.Danger, "deny${event.interaction.user.id}"){
                                        label = "Запрет"
                                        emoji = DiscordPartialEmoji(name = "❌")
                                    }

                                    interactionButton(ButtonStyle.Danger, "close"){
                                        label = "Закрыть"
                                        emoji = DiscordPartialEmoji(name = "\uD83D\uDDC4")
                                    }
                                }
                            }

                            event.interaction.respondPublic {
                                content = "Модератор ${event.interaction.user.username}#${event.interaction.user.discriminator} снял запрет на создание жалоб у юзера ${member.username}#${member.discriminator}!"
                            }
                        }
                    } else {
                        event.interaction.respondEphemeral {

                            content = ":x: У тебя нет прав на это!"
                        }
                    }
                } else if (event.interaction.componentId == "close"){

                    if (event.interaction.user.asMember(guildFor(event)!!.id).roleIds.contains(Snowflake(695711294061412463))) {
                        event.interaction.message!!.edit {
                            content = "Данный репорт закрыт, и не требует рассмотрения!"

                            components = mutableListOf()
                        }

                        event.interaction.respondPublic {
                            content = "Модератор ${event.interaction.user.username}#${event.interaction.user.discriminator} закрыл репорт! (https://discord.com/channels/${event.interaction.message!!.getGuild().id.value}/${event.interaction.message!!.channel.id.value}/${event.interaction.message!!.id.value})"

                            actionRow {
                                linkButton("https://discord.com/channels/${event.interaction.message!!.getGuild().id.value}/${event.interaction.message!!.channel.id.value}/${event.interaction.message!!.id.value}"){
                                    label = "Репорт"
                                    emoji = DiscordPartialEmoji(name = "\uD83D\uDD17")
                                }
                            }
                        }
                    } else {
                        event.interaction.respondEphemeral {

                            content = ":x: У тебя нет прав на это!"
                        }
                    }
                }
            }
        }
    }
}