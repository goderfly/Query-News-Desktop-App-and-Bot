import com.github.kittinunf.fuel.httpGet
import org.jsoup.Jsoup


object ReceiveNewsByQueryInteractor {
    data class NewsItem (
        val header: String,
        val subText: String,
        val author: String,
        val time: String,
        val href: String
    )

    fun getNewsForQuery(query: String, onNewsParsed: (List<NewsItem>) ->  Unit){
        val httpAsync = "https://newssearch.yandex.ru/news/search?text=$query&flat=1"
            .httpGet()
            .header(
                "cookie" to "yuidss=3909118961646237914; yandexuid=3909118961646237914; is_gdpr=0; is_gdpr_b=CJv7eBCbZQ%3D%3D; _yasc=4iZcOb6Nfi9F7IXBn4CCW06tnwfHYsdCw4s73deRHZNo1Q%3D%3D; i=51P3PgPyUm1EWQFxO%2BOm%2FFlhRH4yzeu18gKHFweMczcR8YxO2%2B4ASu0Z7jqKQZkSBF0XwUUkWE%2B4l7QSpYiwUsa8c%2BU%3D; spravka=dD0xNjE0NzAxOTc0O2k9MTI4LjY4LjQ1Ljc3O0Q9RkM3RUFBODJCMEE2Q0IwNjJGRjhFRTRGMTgzNzk1NEQxRkU1N0VCRDg0MDFFRDc2M0RGQUVFNTk3QkJBODlGRkU2QkI4OTgzO3U9MTYxNDcwMTk3NDgzNzQyMjc0NztoPWJjNDcwOTI2NjVmMWJmN2Q5YzUxNDQ0ZjhmZGMyMTBm",
                "Connection" to "keep-alive",
                "Upgrade-Insecure-Requests" to "1",
                "User-Agent" to "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4647.0 Mobile Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
                "Sec-Fetch-Site" to "same-site",
                "Sec-Fetch-Mode" to "navigate",
                "Sec-Fetch-User" to "?1",
                "Sec-Fetch-Dest" to "document",
                "Referer" to "https://yandex.ru/news/region/orel",
                "Accept-Language" to "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
                "Cookie" to "news_lang=ru; nc=tips=1627144982743%3Bfavorites-button:1#search-visits-per-week=5:1646237679000; yandexuid=4208898961623530244; mda=0; is_gdpr=0; is_gdpr_b=CPT0GBDjOygC; gdpr=0; _ym_uid=1627144943997823017; my=YwA=; L=fFMHVAdVfX5qB1JTVmRRT1pWXmAKTFNMAyQ3NwcfAj8=.1638548779.14815.382191.e6b169ced77218b4398d3dfd6cf08ce4; yandex_login=Goderfly; i=Q60AGOKDwfo6KWAnRb1DIyr21puRojtEcEbylOxIvTH/C1hZDzVTX3OOzrgOggzflo+0+xGi35Fi0DxU79ES1268ZtE=; yandex_gid=10; Session_id=3:1646220332.5.0.1627144964457:l1dRXQ:24.1.2:1|51470467.0.2|1130000022627278.11397087.2.2:11397087|3:248845.837714.LYbN93oW8RQDQVOgIpnq-0wRQb0; sessionid2=3:1646220332.5.0.1627144964457:l1dRXQ:24.1.2:1|51470467.0.2|1130000022627278.11397087.2.2:11397087|3:248845.837714.LYbN93oW8RQDQVOgIpnq-0wRQb0; _ym_isad=1; KIykI=1; yabs-frequency=/5/00040000002DWTnX/7Q0_ROO0001iHK62OK5jXW0006n58r7lnHVa9vzpR4M07SvxfH5gytriHI0hVo5NAvyJUcn5O1DhALXN0000R4LWO-2FhFPgncbiHI2ZDMGrP57RScn586DAzBnKAfvuR4NWbqyobYOQBafiHS3x4XnmaW0006n58000/; font_loaded=YSv1; yuidss=4208898961623530244; ymex=1961597628.yrts.1646237628#1938890244.yrtsi.1623530244; _ym_d=1646237683; _yasc=DP5qIHgc+e9IB4bISgOeBmioTyaisxQtJkxvDxWlRtxeROKvzNuXU5onnZ4=; yp=1662005687.szm.1:3840x1200:2235x1057#1953908779.udn.cDpCb3JpcyBNaXJvbm92#1675099768.p_sw.1643563767#1953902051.multib.1#1670084911.nt.computers#1646845095.ygu.1#1648812690.spcs.d; device_id=a3d35f6087a8f10c2ca8bf7367748d7d1d55872c4; active-browser-timestamp=1646237836658"

        )
            .responseString { request, response, result ->
                val doc = Jsoup.parse(result.get())
                val newsItems = doc
                    .select(".news-search-story")
                    .map {
                        NewsItem(
                            header = it?.getElementsByClass("mg-snippet__url")?.first()?.text() ?: "",
                            subText = it?.getElementsByClass("mg-snippet__text")?.first()?.text() ?: "",
                            author = it?.getElementsByClass("mg-snippet-source-info__agency-name")?.first()?.text() ?: "",
                            time = it?.getElementsByClass("mg-snippet-source-info__time")?.first()?.text() ?: "",
                            href = it?.getElementsByClass("mg-snippet__url")?.first()?.attr("href") ?: "",
                        )
                }
                onNewsParsed.invoke(newsItems)
            }

        httpAsync.join()
    }

}