package com.example.gaia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"

        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LASTNAME = "lastname"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_FIRST_LOGIN = "firstLogin"
    }

    fun isFirstLogin(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT firstLogin FROM users WHERE username = ?", arrayOf(username))
        return if (cursor.moveToFirst()) {
            cursor.getInt(0) == 1 // 1 se for primeiro login, 0 caso contrário
        } else {
            true // Se não encontrar o usuário, considera como primeiro login
        }.also {
            cursor.close()
        }
    }

    fun setFirstLoginCompleted(username: String) {
        val db = this.writableDatabase
        db.execSQL("UPDATE users SET firstLogin = 0 WHERE username = ?", arrayOf(username))
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USERNAME TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LASTNAME TEXT," +
                "$COLUMN_PASSWORD TEXT)" +
                "$COLUMN_FIRST_LOGIN INTEGER DEFAULT 1)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_FIRST_LOGIN INTEGER DEFAULT 1")
        }
    }

    // Método para adicionar um usuário
    fun addUser(username: String, name: String, lastname: String, password: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_NAME, name)
            put(COLUMN_LASTNAME, lastname)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_FIRST_LOGIN, 1)
        }
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    // Método para verificar o login
    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, null, "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
            arrayOf(username, password), null, null, null)

        return cursor.count > 0
    }
}