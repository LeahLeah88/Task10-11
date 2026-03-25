package com.example.roomdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: com.example.roomdemo.db.ProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).productDao()

        binding.btnAdd.setOnClickListener {
            lifecycleScope.launch {
                val message = withContext(Dispatchers.IO) {
                    val id = dao.insert(
                        name = "Яблоко",
                        category = "Фрукты",
                        quantity = 10
                    )
                    "Добавлено: id=$id"
                }
                binding.txtOutput.text = message
            }
        }

        binding.btnLoad.setOnClickListener {
            lifecycleScope.launch {
                val rows = withContext(Dispatchers.IO) { dao.getAll() }
                binding.txtOutput.text = if (rows.isEmpty()) {
                    "В базе пока пусто"
                } else {
                    rows.joinToString(separator = "\n") { row ->
                        "${row.id}. ${row.name} [${row.category}] qty=${row.quantity}"
                    }
                }
            }
        }

        binding.btnClear.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) { dao.clear() }
                binding.txtOutput.text = "Очищено"
            }
        }
    }
}

