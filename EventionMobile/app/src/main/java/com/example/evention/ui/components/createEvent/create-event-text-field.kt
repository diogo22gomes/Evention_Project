import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CustomCreateEventTextField(
    label: String,
    value: String,
    description: Boolean = false,
    isPrice: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val maxChars = when {
        description -> 500
        isPrice -> 15
        else -> 70
    }
    val height = if (description) 150.dp else 56.dp

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxChars) {
                if (isPrice) {
                    // formatos de . e ,
                    val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                        .replace(',', '.')

                    val formatted = filtered.toDoubleOrNull()?.let {
                        String.format("%.2f€", it)
                    } ?: ""
                    onValueChange(formatted)
                } else {
                    // Para o texto normal e descrição
                    onValueChange(newValue)
                }
            }
        },
        label = { Text(label, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(height),
        singleLine = !description,
        maxLines = if (description) 5 else 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black
        ),
        keyboardOptions = if (isPrice) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default
    )
}
