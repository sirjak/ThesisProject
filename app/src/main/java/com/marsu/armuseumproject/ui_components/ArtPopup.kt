import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.marsu.armuseumproject.database.Artwork

@Composable
fun ArtPopup(art: Artwork, onDismiss: () -> Unit) {
    /*Popup(
        alignment = Alignment.Center,
        onDismissRequest = { Log.d("DISMISS", "Dismiss request detected") }, content = {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.tertiary
                ), modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 60.dp)
            ) {
                Text(text = "At least this works")
            }
        })*/
    Log.d("POPUP art", art.toString())
    Column {
        Dialog(
            content = {
                Card(
                    colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary,
                        disabledContentColor = MaterialTheme.colorScheme.tertiary
                    ), modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 20.dp, top = 60.dp, bottom = 60.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(text = art.title)
                }
            },
            onDismissRequest = {
                Log.d("DISMISS", "Dismiss request detected")
                onDismiss()
            })
    }

}
