import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.request.*
import com.pengrad.telegrambot.request.AnswerInlineQuery
import com.pengrad.telegrambot.request.EditMessageText
import com.pengrad.telegrambot.request.SendMessage


object BotHandler {

    lateinit var bot: TelegramBot

    fun registerTelegramBot() {

        bot = TelegramBot(ContentRepository.telegramBotToken)
        bot.setUpdatesListener {
            val update = it.first()
            println("update $update")

            try {

                if (update.message() != null && update.message()?.viaBot()?.isBot == true) {
                 //todo
                }

                if (update.inlineQuery() != null && update.inlineQuery().query().isNotEmpty()) {
                    val r1 = InlineQueryResultArticle(
                        update.inlineQuery().id(),
                        "Нажмите, что бы отправить команду",
                        InputTextMessageContent("${update.inlineQuery().query()}")
                    )

                    bot.execute(AnswerInlineQuery(update.inlineQuery().id(), r1))

                }

                if (update.callbackQuery() != null) {
                    val callBackData = update.callbackQuery().data()
                    val callBackMessageId = update.callbackQuery().message().messageId().toInt()
                    with(callBackData) {
                        when {
                            startsWith("next_") -> {
                                println("⏭")
                            }
                            startsWith("pause_") -> {
                                println("⏸")
                            }
                            startsWith("previous_") -> {
                                println("⏮")
                            }
                            startsWith("volume-_") -> {
                                println("Валумэ -")
                            }
                            startsWith("volume+_") -> {
                                println("Валумэ +")
                            }
                            startsWith("random_") -> {
                                println("Случайная песня КиШ")
                            }
                            else -> println("unknown callback prefix $callBackData")
                        }
                    }
                    return@setUpdatesListener UpdatesListener.CONFIRMED_UPDATES_ALL
                }

                if (update.message()?.chat()?.id()?.toString() != ContentRepository.telegramGroupId) {
                    return@setUpdatesListener UpdatesListener.CONFIRMED_UPDATES_ALL
                }

                println("onUpdateReceived $update")
            } catch (e: Exception) {
                println("onUpdateReceived exception ${e.message}")
                e.printStackTrace()
            }

            return@setUpdatesListener UpdatesListener.CONFIRMED_UPDATES_ALL
        }

    }

    fun sendNewNotification(text: String?) {
        val l = bot.execute(SendMessage(ContentRepository.telegramGroupId, text).parseMode(ParseMode.HTML))
        println(l.message())
        println(l.isOk)
    }

    fun editPinnedMessage(text: String, volume: String, lyrics: String) {
        val inlineKeyboardMarkup = InlineKeyboardMarkup(
            arrayOf(
                InlineKeyboardButton("⏮").callbackData("previous_"),
                InlineKeyboardButton("⏸").callbackData("pause_"),
                InlineKeyboardButton("⏭").callbackData("next_"),
            ),
            arrayOf(
                InlineKeyboardButton("Громкость -").callbackData("volume-_"),
                InlineKeyboardButton("Громкость +").callbackData("volume+_"),
            ),
            arrayOf(
                InlineKeyboardButton("Случайная песня КиШ").callbackData("random_"),
            )
        )

        bot.execute(
            EditMessageText(
                ContentRepository.telegramGroupId,
                -1,
                "Сейчас играет: $text\nТекущая громкость: $volume\n\n$lyrics"
            ).replyMarkup(inlineKeyboardMarkup)
        )


    }

}