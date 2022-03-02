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
        bot.execute(SendMessage(ContentRepository.telegramGroupId, text).parseMode(ParseMode.HTML))
    }

    fun sendNewNews(newsItem: ReceiveNewsByQueryInteractor.NewsItem) {
        bot.execute(
            SendMessage(ContentRepository.telegramGroupId, createNewsMessage(newsItem)).parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
        )
    }

    fun sendNewNews(newsItem: List<ReceiveNewsByQueryInteractor.NewsItem>) {
        newsItem.forEach {
            bot.execute(
                SendMessage(ContentRepository.telegramGroupId, createNewsMessage(it)).parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
            )
        }

    }

    private fun createNewsMessage(newsItem: ReceiveNewsByQueryInteractor.NewsItem) = "<b>${newsItem.header}</b>" +
            "\n\n${newsItem.subText}" +
            "\n\n<a href=\"${newsItem.href}\">${newsItem.author} ${newsItem.time}</a>"

}