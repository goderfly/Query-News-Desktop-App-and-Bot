// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.WindowClose
import compose.icons.fontawesomeicons.regular.WindowMinimize
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import theme.Colors
import theme.TelegramColors
import theme.typography
import utils.getCenterScreenLocation
import kotlin.system.exitProcess

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {

    val isOpenConfigure = remember { mutableStateOf(!ContentRepository.isBotRegistered) }
    val isOpenNewsMain = remember { mutableStateOf(ContentRepository.isBotRegistered) }
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = getWindowIcon(),
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
        runCatching { BotHandler.registerTelegramBot() }
            .onFailure { println("onFailure registerTelegramBot ${it.printStackTrace()}") }
            .onSuccess { println("success registerTelegramBot") }
    } else {
        println("========")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ConfigureWindow(
    isOpenConfigure: MutableState<Boolean>,
    isOpenNewsMain: MutableState<Boolean>
) {
    val state = rememberWindowState(
        width = Dp.Unspecified,
        height = Dp.Unspecified,
        position = getCenterScreenLocation()
    )

    Window(
        title = "Query Desktop App Configure Screen",
        onCloseRequest = { isOpenConfigure.value = false },
        state = state,
        resizable = false,
        undecorated = true,
        alwaysOnTop = false
    ) {
        Column(
            modifier = Modifier.background(TelegramColors.backgroundMain)
        ) {
            WindowDraggableArea(
                modifier = Modifier.shadow(4.dp)
            ) {
                Box(Modifier.width(528.dp).height(24.dp).background(TelegramColors.backgroundBar))
                Row(
                    modifier = Modifier.width(528.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        Modifier
                            .size(24.dp)
                            .clickable {
                                state.isMinimized = !state.isMinimized
                            }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Regular.WindowMinimize,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(horizontal = 6.dp)
                                .alpha(0.6f)
                        )
                    }
                    Box(
                        Modifier
                            .size(24.dp)
                            .clickable {
                                exitProcess(0)
                            }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Regular.WindowClose,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(horizontal = 6.dp)
                                .alpha(0.6f)
                        )
                    }

                }

            }
            MaterialTheme(
                colors = Colors.material,
                typography = typography,
            ) {
                ConfigureScreen(isOpenConfigure, isOpenNewsMain)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainNewsWindow(
    isOpen: MutableState<Boolean>
) {
    val state = rememberWindowState(
        width = Dp.Unspecified,
        height = Dp.Unspecified,
        position = getCenterScreenLocation()
    )

    Window(
        title = "Query Desktop App News Screen",
        onCloseRequest = { isOpen.value = false },
        state = state,
        resizable = false,
        undecorated = true,
        alwaysOnTop = false,
    ) {

        LaunchedEffect(state) {
            snapshotFlow { state.size }
                .onEach(::onWindowResize)
                .launchIn(this)

            snapshotFlow { state.position }
                .filter { it.isSpecified }
                .onEach(::onWindowRelocate)
                .launchIn(this)
        }

        Column(
            modifier = Modifier.background(TelegramColors.backgroundMain)
        ) {
            WindowDraggableArea(
                modifier = Modifier.shadow(4.dp)
            ) {
                Box(Modifier.width(320.dp).height(24.dp).background(TelegramColors.backgroundBar))
                Row(
                    modifier = Modifier.width(320.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        Modifier
                            .size(24.dp)
                            .clickable {
                                state.isMinimized = !state.isMinimized
                            }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Regular.WindowMinimize,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(horizontal = 6.dp)
                                .alpha(0.6f)
                        )
                    }
                    Box(
                        Modifier
                            .size(24.dp)
                            .clickable {
                                exitProcess(0)
                            }
                    ) {
                        Icon(
                            imageVector = FontAwesomeIcons.Regular.WindowClose,
                            contentDescription = "",
                            tint = Color.LightGray,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(horizontal = 6.dp)
                                .alpha(0.6f))
                    }

                }

            }
            MaterialTheme(
                colors = Colors.material,
                typography = typography,
            ) {
                QueryScreen(isOpen)
            }
        }
    }
}

@Composable
fun getWindowIcon(): Painter {
    return painterResource("ic_news_tray.png")
}

private fun onWindowResize(size: DpSize) {
    println("onWindowResize $size")
}

private fun onWindowRelocate(position: WindowPosition) {
    println("onWindowRelocate $position")
}