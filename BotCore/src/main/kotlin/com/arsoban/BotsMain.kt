package com.arsoban

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import me.desinger.MainBot

@OptIn(DelicateCoroutinesApi::class)
suspend fun main() {
    val token = System.getenv("TOKEN")

    val kordBot = GlobalScope.launch(newSingleThreadContext("kordBot")) {
        val bot = Bot()

        bot.createBot(token)
    }

    val javacordBot = GlobalScope.launch(newSingleThreadContext("javacordBot")) {
        val mainBot = MainBot()

        mainBot.startBot(token)
    }

    kordBot.join()
    javacordBot.join()
}