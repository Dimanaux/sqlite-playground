package com.example.sqliteplayground

import androidx.compose.Composable
import androidx.compose.State
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.foundation.VerticalScroller
import androidx.ui.graphics.Color
import androidx.ui.input.EditorStyle
import androidx.ui.input.ImeAction
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight

val terminalStyle = EditorStyle(
    TextStyle(
        color = Color.Blue,
        fontSize = 16.sp,
        fontFamily = FontFamily.Monospace
    )
)

val title = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)

val code = TextStyle(fontFamily = FontFamily.Monospace)

const val exampleQuery = "SELECT * FROM visits;"

@Composable
fun SQLitePlayground(queryExecutor: (String) -> Relation) {
    val state = +state { EditorModel(exampleQuery) }
    val relationState = +state { queryExecutor(exampleQuery) }

    MaterialTheme {
        Padding(padding = 8.dp) {
            VerticalScroller {
                Column(mainAxisSize = LayoutSize.Expand, modifier = Spacing(8.dp)) {
                    terminalView(state, onSubmit = { query ->
                        queryExecutor(query).apply {
                            if (isNotEmpty()) {
                                relationState.value = this
                            }
                        }
                    })
                    relationView(relationState)
                }
            }
        }
    }
}

@Composable
fun terminalView(state: State<EditorModel>, onSubmit: (String) -> Unit) {
    TextField(
        state.value,
        onValueChange = { new: EditorModel ->
            state.value = if (new.text.any { it == '\n' }) {
                state.value
            } else {
                new
            }
        },
        editorStyle = terminalStyle,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done,
        onImeActionPerformed = { imeAction: ImeAction ->
            if (imeAction == ImeAction.Done) {
                onSubmit(state.value.text)
            }
        }
    )
}

@Composable
fun relationView(relationState: State<Relation>) {
    val (name, header, body) = relationState.value
    Padding(padding = 8.dp) {
        Text(name, style = title)
    }
    if (header.isNotEmpty()) {
        Table(columns = header.size) {
            tableRow(data = header)
            body.forEach { rowData ->
                tableRow(data = rowData)
            }
        }
    }
}

@Composable
fun TableChildren.tableRow(data: List<String>) {
    tableRow {
        data.forEach { value ->
            Text(value, style = code)
        }
    }
}
