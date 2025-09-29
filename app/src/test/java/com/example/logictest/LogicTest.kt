package com.example.logictest

import android.util.Log
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.PriorityQueue

class LogicTest {
    // -----------------------
    // Soal 1: Akai & Franco
    // -----------------------
    fun nilaiMinimal(a: Int, b: Int): Int {
        val total = a + b
        val hasil = if (total >= 100) 0 else 100 - total
        println(hasil)
        return hasil
    }

    @Test
    fun testNilaiMinimal() {
        assertEquals(30, nilaiMinimal(50, 20))
        assertEquals(100, nilaiMinimal(0, 0))
        assertEquals(0, nilaiMinimal(80, 90))
        assertEquals(1, nilaiMinimal(39, 60))
    }

    // -----------------------
    // Soal 2: Akai Panda Matematikawan
    // -----------------------
    // Kalkulator step-by-step (menghormati prioritas operator: * dan : lebih dulu, baru + dan -)
    fun kalkulatorLangkah(expr: String): Int {
        var ekspresi = expr

        fun tampilkan(expr: String, tanda: String) {
            println(expr)
            println(tanda)
        }

        val operatorPrioritas = listOf(listOf('*', ':'), listOf('+', '-'))

        fun ambilAngkaKiri(s: String, idx: Int): Pair<Int, Int> {
            var l = idx - 1
            while (l >= 0 && (s[l].isDigit() || (s[l] == '-' && (l == 0 || "+-*:".contains(s[l - 1]))))) {
                l--
            }
            l++
            return Pair(l, s.substring(l, idx).toInt())
        }

        fun ambilAngkaKanan(s: String, idx: Int): Pair<Int, Int> {
            var r = idx + 1
            if (r < s.length && s[r] == '-') r++
            while (r < s.length && s[r].isDigit()) {
                r++
            }
            return Pair(r - 1, s.substring(idx + 1, r).toInt())
        }

        for (ops in operatorPrioritas) {
            var i = 0
            while (i < ekspresi.length) {
                val c = ekspresi[i]

                if (c in ops && !(c == '-' && (i == 0 || "+-*:".contains(ekspresi[i - 1])))) {
                    val (lIdx, kiri) = ambilAngkaKiri(ekspresi, i)
                    val (rIdx, kanan) = ambilAngkaKanan(ekspresi, i)

                    val hasil = when (c) {
                        '+' -> kiri + kanan
                        '-' -> kiri - kanan
                        '*' -> kiri * kanan
                        ':' -> kiri / kanan
                        else -> 0
                    }

                    val baru = ekspresi.substring(0, lIdx) + hasil.toString() + ekspresi.substring(rIdx + 1)
                    val tanda = ".".repeat(lIdx) + "-".repeat(rIdx - lIdx + 1) + ".".repeat(ekspresi.length - (rIdx + 1))
                    tampilkan(ekspresi, tanda)

                    ekspresi = baru
                    i = -1
                }
                i++
            }
        }
        println(ekspresi)
        return ekspresi.toInt()
    }

    @Test
    fun testKalkulatorLangkah() {
        assertEquals(16, kalkulatorLangkah("23+16-8*3+4:3"))
        assertEquals(0, kalkulatorLangkah("42+-42"))
        assertEquals(-8, kalkulatorLangkah("-5+-3"))
    }

    // -----------------------
    // Soal 3: Akai Mahasiswa Komputer (CPU Scheduling)
    // -----------------------
    data class Cpu(val id: Int, var selesai: Int = 0, var jumlahTugas: Int = 0) : Comparable<Cpu> {
        override fun compareTo(other: Cpu): Int {
            return if (this.selesai != other.selesai) {
                this.selesai - other.selesai
            } else if (this.jumlahTugas != other.jumlahTugas) {
                this.jumlahTugas - other.jumlahTugas
            } else {
                this.id - other.id
            }
        }
    }

    fun jadwalCpu(n: Int, k: Int, tugas: List<Int>): Int {
        val pq = PriorityQueue<Cpu>()
        for (i in 1..k) pq.add(Cpu(i))

        var waktuAkhir = 0
        for (durasi in tugas) {
            val cpu = pq.poll()
            cpu.selesai += durasi
            cpu.jumlahTugas++
            waktuAkhir = maxOf(waktuAkhir, cpu.selesai)
            pq.add(cpu)
        }
        return waktuAkhir
    }

    @Test
    fun testJadwalCpu() {
        val tugas1 = listOf(8, 3, 2, 5, 2, 2, 2, 5, 3)
        assertEquals(32, jadwalCpu(9, 1, tugas1))
        assertEquals(17, jadwalCpu(9, 2, tugas1))
        assertEquals(12, jadwalCpu(9, 3, tugas1))

        val tugas2 = listOf(10, 1, 5, 200, 30)
        assertEquals(206, jadwalCpu(5, 2, tugas2))
    }
}