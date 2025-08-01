import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.evention.R

@Composable
fun AuthTextField(
    placeholderText: String,
    iconResId: Int,
    onValueChange: (String) -> Unit,
    value: String,
    password: Boolean,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color(0xFF747688)
            )
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = placeholderText,
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            if (password) {
                val image = if (passwordVisible) R.drawable.eye else R.drawable.hiddeneye
                val description = if (passwordVisible) "Ocultar senha" else "Mostrar senha"

                Image(
                    painter = painterResource(id = image),
                    contentDescription = description,
                    modifier = Modifier
                        .clickable { passwordVisible = !passwordVisible }
                        .size(24.dp)
                )
            }
        },
        visualTransformation = if (password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFFE4DFDF),
            unfocusedIndicatorColor = Color(0xFFE4DFDF),
            focusedContainerColor = Color(0xFFFFFFFF),
            unfocusedContainerColor = Color(0xFFFFFFFF)
        )
    )
}