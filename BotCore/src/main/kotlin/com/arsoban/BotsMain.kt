package com.arsoban

import com.arsoban.prokyrorapi.startServer
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import me.desinger.MainBot

@OptIn(DelicateCoroutinesApi::class)
suspend fun main() {
    var token = System.getenv("TOKEN").takeUnless { text ->
        text == ""
    }

    if (token == null) {
        token = dotenv()["TOKEN"]
    }

    var restPort = System.getenv("PORT").takeUnless { text ->
        text == ""
    }

    if (restPort == null) {
        restPort = dotenv()["PORT"]
    }

    val kordBot = GlobalScope.launch(newSingleThreadContext("kordBot")) {
        val bot = Bot()

        bot.createBot(token!!)
    }

    val javacordBot = GlobalScope.launch(newSingleThreadContext("javacordBot")) {
        val mainBot = MainBot()

        mainBot.startBot(token)
    }

    val restApi = GlobalScope.launch(newSingleThreadContext("restApi")) {

        startServer(restPort!!.toInt())

    }

    kordBot.join()
    javacordBot.join()
    restApi.join()
}