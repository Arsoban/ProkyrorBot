package com.arsoban.extension.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalUser
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.rest.builder.message.create.embed

class AvatarExtension : Extension() {

    inner class AvatarArgs : Arguments() {
        val user by optionalUser("user", "Пользователь чей аватар вы хотите посмотреть", false)
    }

    override val name: String = "AvatarExtension"

    override suspend fun setup() {
        publicSlashCommand(::AvatarArgs) {

            name = "avatar"

            description = "Показывает аватар пользователя"

            guild(621722954002333696)

            action {
                if (arguments.user != null){
                    respond {
                        embed {
                            title = "Аватар"

                            color = Color(0, 255, 0)

                            image = arguments.user!!.avatar!!.url

                            footer {
                                text = "${arguments.user!!.username}#${arguments.user!!.discriminator}"
                                icon = arguments.user!!.avatar!!.url
                            }
                        }
                    }
                } else {
                    respond {
                        embed {
                            title = "Аватар"

                            color = Color(0, 255, 0)

                            image = event.interaction.user.asUser().avatar!!.url

                            footer {
                                text = "${event.interaction.user.asUser().username}#${event.interaction.user.asUser().discriminator}"
                                icon = event.interaction.user.asUser().avatar!!.url
                            }
                        }
                    }
                }
            }

        }
    }
}