package com.example.cupcake.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.TextFieldDetail
import com.example.cupcake.ui.components.ButtonComponent
import com.example.cupcake.ui.components.CheckBoxComponent
import com.example.cupcake.ui.components.HeadingTextComponent
import com.example.cupcake.ui.components.MyTextField
import com.example.cupcake.ui.components.NormalTextComponent
import kotlin.math.log


@Composable
fun SignUpScreen(modifier: Modifier = Modifier,
                 textFieldDetails: List<TextFieldDetail>,
                 viewModel: LoginViewModel,
                 onLoginClick: (Int) -> Unit = {},
                 onRegister: () -> Unit
                 ){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(28.dp)
            .animateContentSize(
                animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessMedium)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        NormalTextComponent(value = stringResource(id = R.string.hello_there))
        HeadingTextComponent(value = stringResource(R.string.create_an_account))

        textFieldDetails.forEach { it ->
            Spacer(modifier = Modifier.height(8.dp))
            MyTextField(
                labelValue = stringResource(id = it.label),
                icon = it.icon,
                keyboardOptions = it.keyBoardType,
                onKeyboardDone = {str ->
                    when (it.label) {
                        R.string.first_name -> {
                            viewModel.updateFirstName(str)
                        }
                        R.string.last_name -> {
                            viewModel.updateLastName(str)
                        }
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
        CheckBoxComponent(value = stringResource(id = R.string.tnc),
                checkBoxChecked = {viewModel.updateCheckBox()}
            )

        Spacer(modifier = Modifier.height(16.dp))
        ButtonComponent(value = stringResource(id = R.string.register),
            enabled = viewModel.canRegister(),
            onClick = { onRegister() }
            )

        Spacer(modifier = Modifier.weight(1f))
        Row {
            Text(text = "Already Have An Account?", modifier = Modifier.padding(end = 4.dp))

            ClickableText(text = AnnotatedString(stringResource(id = R.string.login)),
                onClick = onLoginClick,
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline)
            )
        }



    }

}

@Preview
@Composable
fun SignUpScreenPreview(){
    CupcakeTheme {
        
    }
}


