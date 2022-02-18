// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import theme.Colors
import theme.typography
import utils.getCenterScreenLocation
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {

    val isOpenConfigure = remember { mutableStateOf(!ContentRepository.isBotRegistered) }
    val isOpenNewsMain = remember { mutableStateOf(ContentRepository.isBotRegistered) }

    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = getWindowIcon(),
        hint = "Query Desktop App",
        menu = {
            Item(
                text = "Тест",
                onClick = {
                    BotHandler.sendNewNotification("test")
                }
            )
            Item(
                text = "Сменить конфигурацию",
                onClick = {
                    isOpenConfigure.value = true
                }
            )
            Item(
                text = "Выход",
                onClick = {
                    exitProcess(0)
                }
            )
        }
    )

    if (isOpenConfigure.value) {
        ConfigureWindow(isOpenConfigure, isOpenNewsMain)
    }
    if (isOpenNewsMain.value) {
        MainNewsWindow(isOpenNewsMain)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ConfigureWindow(
    isOpenConfigure: MutableState<Boolean>,
    isOpenNewsMain: MutableState<Boolean>
) {

    Window(
        title = "Query Desktop App Configure Screen",
        onCloseRequest = { isOpenConfigure.value = false },
        state = rememberWindowState(
            size = WindowSize(606.dp, 260.dp),
            position = getCenterScreenLocation()
        ),
        resizable = false,
        alwaysOnTop = false
    ) {
        MaterialTheme(
            colors = Colors.material,
            typography = typography,
        ) {
            ConfigureScreen(isOpenConfigure, isOpenNewsMain)
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainNewsWindow(
    isOpen: MutableState<Boolean>
) {

    Window(
        title = "Query Desktop App News Screen",
        onCloseRequest = { isOpen.value = false },
        state = rememberWindowState(
            size = WindowSize(606.dp, 260.dp),
            position = getCenterScreenLocation()
        ),
        resizable = false,
        alwaysOnTop = false
    ) {
        MaterialTheme(
            colors = Colors.material,
            typography = typography,
        ) {
            QueryScreen(isOpen)
        }

    }
}


fun getWindowIcon(): BufferedImage {
    return runCatching {
        ImageIO.read(File("src/main/resources/ic_news_tray.png"))
    }.getOrElse {
        BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    }
}