package no.nordicsemi.android.material.you

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.textfield.TextInputLayout

@Composable
fun TextField(text: String, hint: String, onTextChanged: (String) -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { createEditText(it, hint, onTextChanged) },
        update = {
            it.editText?.apply {
                setText(text)
                setSelection(text.length)
            }
        }
    )
}

private fun createEditText(context: Context, hint: String, onTextChanged: (String) -> Unit): TextInputLayout {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.input_text, null, false) as TextInputLayout

    view.hint = hint
    view.editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s?.toString() ?: "")
        }

        override fun afterTextChanged(s: Editable?) { }
    })

    return view
}
