import utils.toJson
import utils.toObject
import java.util.*

object RefreshNewsInteractor {
    var lastNewsItem: ReceiveNewsByQueryInteractor.NewsItem
        set(value) { ContentRepository.lastNewsItem = value.toJson() }
        get() = ContentRepository.lastNewsItem.toObject()

    fun startGettingUpdateNews(query: String, onNewNews: (ReceiveNewsByQueryInteractor.NewsItem) -> Unit) {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                println("Проверка новостей $this")
                ReceiveNewsByQueryInteractor.getNewsForQuery(query) {
                    if (lastNewsItem != it.first()) {
                        lastNewsItem = it.first()
                        onNewNews.invoke(it.first())
                    }
                }

            }
        }, 6000, 6000)
    }

}