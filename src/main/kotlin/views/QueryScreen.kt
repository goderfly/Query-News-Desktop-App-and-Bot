import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import theme.TelegramColors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QueryScreen(
    isOpen: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.background(TelegramColors.leftBarSelection)
    ) {
        OutlinedTextField(
            modifier = Modifier.padding(PaddingValues(16.dp, 8.dp)).fillMaxWidth(),
            value = ContentRepository.lastQuery,
            enabled = true,
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
            },
            label = {
                Text(
                    color = Color.White,
                    text = "Введите запрос"
                )
            }
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth().padding(PaddingValues(16.dp, 8.dp, 16.dp, 16.dp)),
            onClick = {

            }
        ) {
            Text("Найти")
        }
    }


}
