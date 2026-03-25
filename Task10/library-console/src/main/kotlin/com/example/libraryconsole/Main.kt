package com.example.libraryconsole

data class Book(
    val id: Int,
    val title: String,
    var borrowedBy: String? = null
) {
    val isAvailable: Boolean
        get() = borrowedBy == null
}

class Library {
    private val books: MutableMap<Int, Book> = linkedMapOf()

    fun addBook(book: Book) {
        require(book.id > 0) { "id must be > 0" }
        if (books.containsKey(book.id)) {
            throw IllegalArgumentException("Book with id=${book.id} already exists")
        }
        books[book.id] = book
    }

    fun borrow(bookId: Int, borrower: String): Boolean {
        val book = books[bookId] ?: return false
        if (!book.isAvailable) return false
        book.borrowedBy = borrower
        return true
    }

    fun returnBook(bookId: Int, borrower: String): Boolean {
        val book = books[bookId] ?: return false
        if (book.borrowedBy != borrower) return false
        book.borrowedBy = null
        return true
    }

    fun printState() {
        if (books.isEmpty()) {
            println("Библиотека пуста")
            return
        }

        println("Состояние библиотеки:")
        books.values.forEach { book ->
            val status = if (book.isAvailable) "Доступна" else "Взята: ${book.borrowedBy}"
            println(" - [${book.id}] ${book.title} => $status")
        }
    }
}

interface ReaderActions {
    fun borrow(bookId: Int): Boolean
    fun returnBook(bookId: Int): Boolean
}

interface AdminActions {
    fun addNewBook(bookId: Int, title: String)
}

class ReaderActionsDelegate(
    private val library: Library,
    private val readerName: String
) : ReaderActions {
    override fun borrow(bookId: Int): Boolean =
        library.borrow(bookId, readerName)

    override fun returnBook(bookId: Int): Boolean =
        library.returnBook(bookId, readerName)
}

class AdminActionsDelegate(
    private val library: Library
) : AdminActions {
    override fun addNewBook(bookId: Int, title: String) {
        library.addBook(Book(id = bookId, title = title))
        println("Администратор добавил книгу: [$bookId] $title")
    }
}

// Делегирование интерфейсов для уменьшения дублирования кода.
class Reader(
    private val library: Library,
    private val name: String
) : ReaderActions by ReaderActionsDelegate(library, name)

class Admin(
    private val library: Library
) : AdminActions by AdminActionsDelegate(library)

fun main() {
    val library = Library()

    val admin = Admin(library)
    val reader = Reader(library, "Алексей")

    admin.addNewBook(1, "Война и мир")
    admin.addNewBook(2, "Мастер и Маргарита")
    library.printState()
    println()

    println("Читатель пытается взять книгу #1...")
    println("Результат: ${reader.borrow(1)}")
    library.printState()
    println()

    println("Читатель пытается взять книгу #1 ещё раз...")
    println("Результат: ${reader.borrow(1)}")
    println()

    println("Читатель возвращает книгу #1...")
    println("Результат: ${reader.returnBook(1)}")
    library.printState()
    println()

    println("Читатель берёт книгу #2...")
    println("Результат: ${reader.borrow(2)}")
    library.printState()
}

