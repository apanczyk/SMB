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
    private lateinit var sp: SharedPreferences
    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)
        loadSharedPreferences()
    }

    private fun loadSharedPreferences() {
        sp = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE)

        val color = sp.getInt(Options.COLOR, Options.BASIC_COLOR)
        val size = sp.getFloat(Options.SIZE, Options.BASIC_SIZE)
        options = Options(color, size)
        setValues(color, size)
    }

    private fun setValues(color: Int, size: Float) {
        val colors = resources.getStringArray(R.array.color_list)
        val fontSize = findViewById<EditText>(R.id.editTextNumber)
        val spinner = findViewById<Spinner>(R.id.colorSpinner)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        fontSize.text = size.toString().toEditable()

        spinner.setSelection(
            (spinner.adapter as ArrayAdapter<String?>).getPosition(
                Options.fromColor(color)
            )
        )
    }

    fun save(view: View) {
        options.color = Options.colorOf(this.view.colorSpinner.selectedItem.toString())
        options.size = this.view.editTextNumber.text.toString().toFloat()

        val editor = getSharedPreferences(Options.PREFERENCES, MODE_PRIVATE).edit()
        editor.putInt(Options.COLOR, options.color)
        editor.putFloat(Options.SIZE, options.size)
        editor.apply()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
private fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
