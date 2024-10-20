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
        data class uveg(
            val nev: String,
            val terfogat: Int,
            val ertek: Int
        )

        // UI-ból adatok lekérése.
        val savingsInput = findViewById<EditText>(R.id.SavingsInput_editTextNumber)
        val volumeInput = findViewById<EditText>(R.id.VolumeInput_editTextNumber)
        val resultText = findViewById<TextView>(R.id.Result_TextView)
        val calculateButton = findViewById<Button>(R.id.CalculateButton_Button)

        // Üvegek listája.
        val uvegek = listOf(
            uveg("Sörösüveg", 500, 60),
            uveg("0,5 literes műanyag", 500, 50),
            uveg("2 literes műanyag", 2000, 80)
        )

        // Számitás lefuttatása a gomb lenyomásakor.
        calculateButton.setOnClickListener {
            val megtakaritas = savingsInput.text.toString().toIntOrNull() ?: 0
            val csomagterTerfogat = volumeInput.text.toString().toIntOrNull() ?: 0

            if (megtakaritas <= 0 || csomagterTerfogat <= 0) {
                resultText.text = "Hibás adat! A megtakarítás és a csomagtartó térfogatának pozitív számnak kell lennie."
                return@setOnClickListener
            }

            // Számítás
            var osszTerfogat = 0
            var osszErtek = 0
            val uvegDarabok = mutableListOf<Pair<String, Int>>()

            for (uveg in uvegek) {
                val darabszam = megtakaritas / uveg.ertek
                osszErtek += darabszam * uveg.ertek
                uvegDarabok.add(Pair(uveg.nev, darabszam))
                osszTerfogat += darabszam * uveg.terfogat
            }

            // Szükséges fordulók kiszámítása
            val eredmeny = StringBuilder()

            if (osszErtek >= megtakaritas) {
                if (osszTerfogat <= csomagterTerfogat) {
                    eredmeny.append("Egy forduló elég lesz. Összesen $osszTerfogat cm³ térfogatot foglalnak az üvegek.\n")
                }

                else {
                    val fordulok = ceil(osszTerfogat.toDouble() / csomagterTerfogat).toInt()
                    eredmeny.append("$fordulok fordulóra lesz szükséged.\n")
                }

                eredmeny.append("Az üvegek elosztása:\n")
                for ((nev, darab) in uvegDarabok) {
                    eredmeny.append("$darab darab $nev\n")
                }
            }

            else {
                eredmeny.append("Nincs elég üveg az adott megtakarítás eléréséhez.")
            }

            // Eredmény megjelítése
            resultText.text = eredmeny.toString()
        }
    }
}

