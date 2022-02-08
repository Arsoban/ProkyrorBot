package com.arsoban.extension.command

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.long
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.commands.converters.impl.user
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.*
import dev.kord.common.entity.Snowflake
import kotlin.time.Duration.Companion.minutes

class TimeoutExtension : Extension() {

    inner class TimeoutArgs : Arguments() {

        val target by user {
            name = "target"
            description = "Пользователь которому вы хотите выдать таймаут"
        }

        val time by long {

            name = "minutes"

            description = "На сколько минут вы хотите выдать таймаут"

        }

        val reason by string {

            name = "reason"

            description = "Причина таймаута"

        }

    }

    override val name: String = "TimeoutExtension"

    override suspend fun setup() {

        publicSlashCommand(::TimeoutArgs) {

            name = "timeout"

            description = "Выдаёт таймаут"

            guild(621722954002333696)

            action {

                val target = arguments.target.asMember(guild!!.id)


                if (member!!.asMember().roleIds.contains(Snowflake(850003754887675914))) {

                    if (member!!.asMember() != target) {

                        target.timeout(until = arguments.time.minutes, reason = arguments.reason)

                        respond {

                            content = ":white_check_mark: Модератор ${user.asUser().username}#${user.asUser().discriminator} выдал таймаут пользователю ${target.asUser().username}#${target.asUser().discriminator} на ${arguments.time} минут."

                        }

                    } else {

                        respond {

                            content = ":x: Ты не можешь выдать таймаут себе."

                        }

                    }

                } else {

                    respond {

                        content = ":x: У тебя нет прав на эту команду!"

                    }

                }

            }

        }

    }
}