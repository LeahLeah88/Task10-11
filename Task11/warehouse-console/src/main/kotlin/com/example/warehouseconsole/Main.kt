package com.example.warehouseconsole

data class Product(
    val sku: String,
    val name: String,
    val quantity: Int
)

enum class Category {
    FRUITS,
    VEGETABLES,
    DRINKS
}

interface ProductDataSource {
    fun loadProducts(category: Category): List<Product>
}

class MockProductDataSource : ProductDataSource {
    override fun loadProducts(category: Category): List<Product> {
        println("LAZY: подгружаю товары категории $category...")
        return when (category) {
            Category.FRUITS -> listOf(
                Product("FR-01", "Яблоко", 120),
                Product("FR-02", "Банан", 80)
            )

            Category.VEGETABLES -> listOf(
                Product("VE-01", "Морковь", 200),
                Product("VE-02", "Огурец", 150)
            )

            Category.DRINKS -> listOf(
                Product("DR-01", "Сок", 60),
                Product("DR-02", "Вода", 300)
            )
        }
    }
}

interface StockReporter {
    fun formatCategory(category: Category, products: List<Product>): String
}

class ConsoleStockReporter : StockReporter {
    override fun formatCategory(category: Category, products: List<Product>): String {
        val total = products.sumOf { it.quantity }
        return buildString {
            appendLine("Категория: $category")
            products.forEach { p ->
                appendLine(" - ${p.name} (${p.sku}) qty=${p.quantity}")
            }
            appendLine("Итого в категории: $total")
        }
    }
}

class ProductCompany(
    private val dataSource: ProductDataSource,
    reporter: StockReporter = ConsoleStockReporter()
) : StockReporter by reporter {
    // Подгрузка данных о каждой категории происходит только при первом обращении.
    val fruits: List<Product> by lazy { dataSource.loadProducts(Category.FRUITS) }
    val vegetables: List<Product> by lazy { dataSource.loadProducts(Category.VEGETABLES) }
    val drinks: List<Product> by lazy { dataSource.loadProducts(Category.DRINKS) }
}

fun main() {
    val company = ProductCompany(MockProductDataSource())

    println("Система запущена. Пока не обращаемся к категориям...")
    println()

    println("Первый доступ к `fruits` (должна произойти ленивой загрузка):")
    println(company.formatCategory(Category.FRUITS, company.fruits))
    println()

    println("Второй доступ к `fruits` (загрузка НЕ должна повториться):")
    println(company.formatCategory(Category.FRUITS, company.fruits))
    println()

    println("Доступ к `drinks` (ленивой загрузка должна сработать для drinks):")
    println(company.formatCategory(Category.DRINKS, company.drinks))
}

