package com.peroni.bottlebank

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Alap adatbázis.
        data class bottle(
            val name: String,
            val volume: Int,
            val value: Int
        )

        // UI-ból adatok lekérése
        val savingsInput = findViewById<EditText>(R.id.SavingsInput_editTextNumber)
        val volumeInput = findViewById<EditText>(R.id.VolumeInput_editTextNumber)
        val resultText = findViewById<TextView>(R.id.Result_TextView)
        val calculateButton = findViewById<Button>(R.id.CalculateButton_Button)

        // Üvegek listája
        val bottles = listOf(
            bottle("Sörösüveg", 500, 60),
            bottle("0,5 literes műanyag", 500, 50),
            bottle("2 literes műanyag", 2000, 80)
        )

        // Számitás lefuttatása a gomb lenyomásakor
        calculateButton.setOnClickListener {
            val savings = savingsInput.text.toString().toIntOrNull() ?: 0
            val storageVolume = volumeInput.text.toString().toIntOrNull() ?: 0

            if (savings <= 0 || storageVolume <= 0) {
                resultText.text = "Hibás adat! A megtakarítás és a csomagtartó térfogatának pozitív számnak kell lennie."
                return@setOnClickListener
            }

            // Számítás folyamata
            var totalVolume = 0
            var totalValue = 0
            val pieceofBottles = mutableListOf<Pair<String, Int>>()

            for (bottle in bottles) {
                val pieces = savings / bottle.value
                totalValue += pieces * bottle.value
                pieceofBottles.add(Pair(bottle.name, pieces))
                totalVolume += pieces * bottle.volume
            }

            // Szükséges fordulók kiszámítása
            val result = StringBuilder()

            if (totalValue >= savings) {
                if (totalVolume <= storageVolume) {
                    result.append("Egy forduló elég lesz. Összesen $totalVolume cm³ térfogatot foglalnak az üvegek.\n")
                }

                else {
                    val turns = ceil(totalVolume.toDouble() / storageVolume).toInt()
                    result.append("$turns fordulóra lesz szükséged.\n")
                }

                result.append("Az üvegek elosztása:\n")
                for ((name, pieces) in pieceofBottles) {
                    result.append("$pieces darab $name\n")
                }
            }

            else {
                result.append("Nincs elég üveg az adott megtakarítás eléréséhez.")
            }

            // Eredmény megjelítése
            resultText.text = result.toString()
        }
    }
}

