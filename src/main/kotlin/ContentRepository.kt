import utils.StorableString
import utils.toJson
import utils.toObject

object ContentRepository {
    var telegramGroupId by StorableString("")
    var telegramBotToken by StorableString("")

    var lastQuery by StorableString("Орёл")
    var lastNewsItem by StorableString()

    var isBotRegistered = telegramBotToken.isNotBlank() && telegramBotToken.isNotBlank()


 /*   fun String.toNewsItem(): ReceiveNewsByQueryInteractor.NewsItem {
        return this.toObject<ReceiveNewsByQueryInteractor.NewsItem>()
    }

    fun ReceiveNewsByQueryInteractor.NewsItem.toJsonString(): String {
        return this.toJson()
    }*/
}