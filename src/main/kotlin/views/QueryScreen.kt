import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.TelegramColors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QueryScreen(
    isOpen: MutableState<Boolean>
) {
    val lastQuery = remember { mutableStateOf(ContentRepository.lastQuery) }

    Column {
        OutlinedTextField(
            modifier = Modifier.padding(PaddingValues(16.dp, 8.dp)).width(288.dp),
            value = lastQuery.value,
            enabled = true,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "image",
                    tint = Color.White
                )
            },
            textStyle = MaterialTheme.typography.body1,
            onValueChange = {
                ContentRepository.lastQuery = it
                lastQuery.value = it
            },
            label = {
                Text(
                    color = Color.White,
                    text = "Введите запрос"
                )
            }
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = TelegramColors.buttonColor
            ),
            modifier = Modifier
                .padding(PaddingValues(16.dp, 8.dp, 16.dp, 16.dp))
                .width(288.dp),
            onClick = {
                ReceiveNewsByQueryInteractor.getNewsForQuery(lastQuery.value) {
                    BotHandler.sendNewNews(it.take(5))
                }
                RefreshNewsInteractor.startGettingUpdateNews(lastQuery.value) {

                }
                isOpen.value = false
            }
        ) {
            Text("Отслеживать")
        }
    }


}
