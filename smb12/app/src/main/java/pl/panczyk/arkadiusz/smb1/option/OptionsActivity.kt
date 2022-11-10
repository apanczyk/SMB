package pl.panczyk.arkadiusz.smb1.option

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import pl.panczyk.arkadiusz.smb1.R
import pl.panczyk.arkadiusz.smb1.databinding.ActivityOptionsBinding


class OptionsActivity : AppCompatActivity() {
    val view by lazy { ActivityOptionsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        setValues()
    }

    private fun setValues() {
        val colors = resources.getStringArray(R.array.color_list)
        val fontSize = findViewById<EditText>(R.id.editTextNumber)
        val spinner = findViewById<Spinner>(R.id.colorSpinner)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        fontSize.text = Options.size.toString().toEditable()

        spinner.setSelection(
            (spinner.adapter as ArrayAdapter<String?>).getPosition(
                Options.fromColor(Options.color)
            )
        )
    }

    fun save(view: View) {
        Options.color = Options.colorOf(this.view.colorSpinner.selectedItem.toString())
        Options.size = this.view.editTextNumber.text.toString().toFloat()

        val editor = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE).edit()
        editor.putInt(Options.COLOR, Options.color)
        editor.putFloat(Options.SIZE, Options.size)
        editor.apply()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
