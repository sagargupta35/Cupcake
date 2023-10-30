package com.example.cupcake.ui.components

import android.widget.CheckBox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cupcake.R
import com.example.cupcake.ui.theme.CupcakeTheme

@Composable
fun NormalTextComponent(value: String){
    Text(text = value,
        modifier = Modifier,
        style = MaterialTheme.typography.titleMedium
        )
}

@Composable
fun HeadingTextComponent(value: String){
    Text(text = value,
        style = MaterialTheme.typography.titleLarge
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyTextField(
    labelValue: String, icon: ImageVector = Icons.Default.Person,
    keyboardOptions: KeyboardOptions,
    onKeyboardDone: (String) -> Unit = {},
){
    var textValue by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        label = {Text(text = labelValue)},
        value = textValue,
        onValueChange = {
            onKeyboardDone(textValue)
            textValue = it },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            cursorColor = Color.Black
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(icon, contentDescription = labelValue)
        },
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onKeyboardDone(textValue) }
        ),


    )
}


@Composable
fun CheckBoxComponent(value: String, checkBoxChecked: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,

    ) {

        var checkedState by rememberSaveable {mutableStateOf(false)}

        Checkbox(checked = checkedState,
                onCheckedChange = {
                    checkBoxChecked()
                    checkedState = !checkedState}
            )

        Text(text = value)

    }
}


@Composable
fun ButtonComponent(value: String, enabled:Boolean = false,
                    onClick: () -> Unit = {}
        ){
    OutlinedButton(onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        contentPadding = PaddingValues(),
        enabled = enabled
        ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.primary
                    )
                ),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ){
            Text(text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp,
                color = Color.White
                )
        }
    }
}

@Preview
@Composable
fun previewFunction(){
    CupcakeTheme {
        ButtonComponent(value = "Register")
    }
}