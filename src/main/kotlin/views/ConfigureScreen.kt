
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import theme.TelegramColors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ConfigureScreen(
    isOpen: MutableState<Boolean>,
    isOpenNewsMain: MutableState<Boolean>
) {
    val telegramBotToken = remember { mutableStateOf(TextFieldValue(ContentRepository.telegramBotToken)) }
    val telegramGroupId = remember { mutableStateOf(TextFieldValue(ContentRepository.telegramGroupId)) }

    val errorTokenState = remember { mutableStateOf(false) }
    val errorGroupIdState = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.background(TelegramColors.backgroundMain)
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(PaddingValues(16.dp, 8.dp)).width(496.dp),
            value = telegramBotToken.value,
            enabled = true,
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "image",
                tint = Color.White
            ) },
            isError = errorTokenState.value,
            textStyle = MaterialTheme.typography.body1,
            onValueChange = {
                ContentRepository.telegramBotToken = it.text
                errorTokenState.value = false
                telegramBotToken.value = it
            },
            label = { Text(color = Color.White, text = "Telegram Bot Token") }
        )
        OutlinedTextField(

            modifier = Modifier.padding(PaddingValues(16.dp, 8.dp)).width(496.dp),
            value = telegramGroupId.value,
            enabled = true,
            leadingIcon = { Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "image",
                tint = Color.White
            ) },
            isError = errorGroupIdState.value,
            textStyle = MaterialTheme.typography.body1,
            onValueChange = {
                ContentRepository.telegramGroupId = it.text
                errorGroupIdState.value = false
                telegramGroupId.value = it
            },
            label = { Text(color = Color.White, text = "Group or User ID") }
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = TelegramColors.buttonColor
            ),
            modifier = Modifier.padding(PaddingValues(16.dp, 8.dp, 16.dp, 16.dp)).width(496.dp),
            onClick = {

                when {
                    telegramBotToken.value.text.isBlank() -> {
                        errorTokenState.value = true
                        return@Button
                    }
                    telegramGroupId.value.text.isBlank() -> {
                        errorGroupIdState.value = true
                        return@Button
                    }
                }

                runCatching { BotHandler.registerTelegramBot() }
                    .onFailure {
                        errorTokenState.value = true
                    }
                    .onSuccess {
                        isOpenNewsMain.value = true
                        isOpen.value = false
                    }
            }
        ) {
            Text("Сохранить")
        }
    }


}
