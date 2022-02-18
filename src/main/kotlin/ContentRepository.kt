import utils.StorableString

object ContentRepository {
    var telegramGroupId by StorableString("")
    var telegramBotToken by StorableString("")

    var lastQuery by StorableString("Орёл")

    val isBotRegistered = telegramBotToken.isNotBlank() && telegramBotToken.isNotBlank()

}