package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

typealias Op = (op: Double) -> Double
typealias OpOp = (op: Double) -> Op

class MainActivity : AppCompatActivity() {

    private var number : Double = 0.0
    private var current : Op? = null
    private var df : DecimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..9) {
            findViewById<Button>(resources.getIdentifier("button_number$i", "id", packageName))
                .setOnClickListener(numberClicked(i));
        }
        df.maximumFractionDigits = 340

        findViewById<Button>(R.id.button_divide).setOnClickListener(execute { j -> { i -> i / j } } )

        findViewById<Button>(R.id.button_minus).setOnClickListener(execute { j -> {  i -> i - j } })

        findViewById<Button>(R.id.button_multiply).setOnClickListener(execute { j -> {  i -> i * j } })

        findViewById<Button>(R.id.button_plus).setOnClickListener(execute { j -> { i -> i + j } })

        findViewById<Button>(R.id.button_equal).setOnClickListener {
            if (current != null) {
                number = current?.invoke(number) ?: number
            }
            findViewById<TextView>(R.id.result_textView).text = df.format(number)
            number = 0.0
            current = null
        }
    }

    private fun numberClicked(i: Int): View.OnClickListener {
        return View.OnClickListener { v: View ->
            number = number*10 + i;
            findViewById<TextView>(R.id.result_textView).text = df.format(number)
        }
    }

    private fun execute(op : OpOp) : View.OnClickListener {
        return View.OnClickListener {
            if (current != null) {
                number = current?.invoke(number) ?: number
                findViewById<TextView>(R.id.result_textView).text = df.format(number)
            }
            current = op.invoke(number)
            number = 0.0
        }
    }
}