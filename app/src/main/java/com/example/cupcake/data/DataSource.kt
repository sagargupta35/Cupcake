/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake.data

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.example.cupcake.R

object DataSource {
    val flavors = listOf(
        R.string.vanilla,
        R.string.chocolate,
        R.string.red_velvet,
        R.string.salted_caramel,
        R.string.coffee
    )

    val quantityOptions = listOf(
        Pair(R.string.one_cupcake, 1),
        Pair(R.string.six_cupcakes, 6),
        Pair(R.string.twelve_cupcakes, 12)
    )

    val textFieldData = listOf(
        TextFieldDetail(R.string.first_name, Icons.Filled.Person, KeyboardOptions(keyboardType = KeyboardType.Text)),
        TextFieldDetail(R.string.last_name, Icons.Filled.Person, KeyboardOptions(keyboardType = KeyboardType.Text)),
        TextFieldDetail(R.string.email, Icons.Filled.Email, KeyboardOptions(keyboardType = KeyboardType.Email)),
        TextFieldDetail(R.string.password, Icons.Filled.Lock, KeyboardOptions(keyboardType = KeyboardType.Password))

    )

    val loginFieldData = listOf(
        TextFieldDetail(R.string.email, Icons.Filled.Email, KeyboardOptions(keyboardType = KeyboardType.Email)),
        TextFieldDetail(R.string.password, Icons.Filled.Lock, KeyboardOptions(keyboardType = KeyboardType.Password))

    )
}

data class TextFieldDetail constructor(@StringRes val label: Int, val icon: ImageVector, val keyBoardType: KeyboardOptions)