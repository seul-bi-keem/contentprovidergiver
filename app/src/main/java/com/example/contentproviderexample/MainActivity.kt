package com.example.contentproviderexample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.contentproviderexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val prefs = getSharedPreferences("userInfo", MODE_PRIVATE)
        val editor = prefs.edit()

        // 데이터 불러오기
        binding.btnLoad.setOnClickListener {
            val stringBuilder = StringBuilder()
            val key = prefs.all
            for (entry in key.entries) {
                var content: String
                if (entry.value != null) {
                    content = if (entry.value!! is String) {
                        entry.value as String
                    } else {
                        entry.value.toString()
                    }
                    val data = "${entry.key} - $content"
                    stringBuilder.append(data)
                    if (stringBuilder.isNotEmpty()) stringBuilder.append("\n")
                }
            }
            binding.tvData.text = stringBuilder.toString()
        }

        // 데이터 추가
        binding.btnAddStr.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.pref_add_dialog_layout, null)
            dialogBuilder.setView(dialogView)
            val keyEditText = dialogView.findViewById<EditText>(R.id.key_edittext)
            val valueEditText = dialogView.findViewById<EditText>(R.id.value_edittext)
            dialogBuilder.setPositiveButton("추가") { _, _ ->
                if (keyEditText.text.isEmpty() || valueEditText.text.isEmpty()) {
                    return@setPositiveButton
                }
                editor.putString(keyEditText.text.toString(), valueEditText.text.toString())
                editor.commit()
            }
            dialogBuilder.create()
            dialogBuilder.show()
        }

        binding.btnAddInt.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.pref_add_int_dialog_layout, null)
            dialogBuilder.setView(dialogView)
            val keyEditText = dialogView.findViewById<EditText>(R.id.key_edittext)
            val valueEditText = dialogView.findViewById<EditText>(R.id.value_edittext)
            dialogBuilder.setPositiveButton("추가") { _, _ ->
                if (keyEditText.text.isEmpty() || valueEditText.text.isEmpty()) {
                    return@setPositiveButton
                }
                editor.putInt(keyEditText.text.toString(), Integer.valueOf(valueEditText.text.toString()))
                editor.commit()
            }
            dialogBuilder.create()
            dialogBuilder.show()
        }

        binding.btnAddBoolean.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.pref_add_boolean_dialog_layout, null)
            dialogBuilder.setView(dialogView)
            val keyEditText = dialogView.findViewById<EditText>(R.id.key_edittext)
            val valueSpinner = dialogView.findViewById<Spinner>(R.id.value_spinner)
            val items = resources.getStringArray(R.array.boolan_prefs_array)
            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
            valueSpinner.adapter = spinnerAdapter
            var valueBoolean = true
            valueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    valueBoolean = position == 0
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
            dialogBuilder.setPositiveButton("추가") { _, _ ->
                if (keyEditText.text.isEmpty()) {
                    return@setPositiveButton
                }
                editor.putBoolean(keyEditText.text.toString(), valueBoolean)
                editor.commit()
            }

            dialogBuilder.create()
            dialogBuilder.show()
        }
    }
}
