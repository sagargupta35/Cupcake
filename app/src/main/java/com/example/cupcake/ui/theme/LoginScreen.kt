package com.example.cupcake.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.TextFieldDetail
import com.example.cupcake.ui.components.ButtonComponent
import com.example.cupcake.ui.components.CheckBoxComponent
import com.example.cupcake.ui.components.HeadingTextComponent
import com.example.cupcake.ui.components.MyTextField
import com.example.cupcake.ui.components.NormalTextComponent

@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                textFieldDetails: List<TextFieldDetail>,
                viewModel: LoginViewModel,
                onLoginClick: () -> Unit = {},
                onRegisterClick: (Int) -> Unit = {}
                ){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        NormalTextComponent(value = stringResource(id = R.string.hello_there))
        HeadingTextComponent(value = stringResource(R.string.welcome_back))

        textFieldDetails.forEach {
            Spacer(modifier = Modifier.height(8.dp))
            MyTextField(
                labelValue = stringResource(id = it.label),
                icon = it.icon,
                keyboardOptions = it.keyBoardType,
                onKeyboardDone = {str ->
                    when (it.label) {
                        R.string.email -> {
                            viewModel.updateEmail(str)
                        }
                        R.string.password -> {
                            viewModel.updatePassword(str)
                        }
                        else -> {
                            viewModel.updateLastName(str)
                        }
                    }
                }
            )
        }

        if(viewModel.failure.isNotEmpty()){
            Text(text = viewModel.failure,
                style = MaterialTheme.typography.body1,
                color = Color.Red
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(value = stringResource(id = R.string.login),
            enabled = viewModel.canLogin(),
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.weight(1f))
        Row {
            Text(text = "Don't have an account?", modifier = Modifier.padding(end = 4.dp))

            ClickableText(text = AnnotatedString(stringResource(id = R.string.register)),
                onClick = onRegisterClick,
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline)
            )
        }



    }

}