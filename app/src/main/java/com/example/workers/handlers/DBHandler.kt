package com.example.workers.handlers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.workers.models.SpecialityModel
import com.example.workers.models.UserModel
import com.example.workers.models.WorkersSpeciality

val DB_NAME = "Workers"
val TABLE_USERS = "Users"
val TABLE_SPECIALITY = "Speciality"
val TABLE_WORKERSSPECIALITY = "WorkersSpeciality"
val USER_ID = "userId"
val USER_NAME = "userName"
val USER_LASTNAME = "userLastName"
val USER_BIRTHDAY = "userBirthday"
val USER_AVATAR = "userAvatar"
val SPECIALITY_ID = "specialityId"
val SPECIALITY_NAME = "specialityName"
val WORKERSSPECIALITY_ID = "workersSpecialityId"
val WORKERSSPECIALITY_USERID = "workersSpecialityUserId"
val WORKERSSPECIALITY_SPECIALITYID = "workersSpecialitySpecialityId"

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = "CREATE TABLE $TABLE_USERS (" +
                "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USER_NAME VARCHAR(256)," +
                "$USER_LASTNAME VARCHAR(256)," +
                "$USER_BIRTHDAY VARCHAR(256) NULL," +
                "$USER_AVATAR VARCHAR(256) NULL)"
        val createSpecialityTable = "CREATE TABLE $TABLE_SPECIALITY (" +
                "$SPECIALITY_ID INTEGER PRIMARY KEY," +
                "$SPECIALITY_NAME VARCHAR(256))"
        val createWorkersSpecialityTable = "CREATE TABLE $TABLE_WORKERSSPECIALITY (" +
                "$WORKERSSPECIALITY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$WORKERSSPECIALITY_USERID INTEGER," +
                "$WORKERSSPECIALITY_SPECIALITYID INTEGER," +
                "FOREIGN KEY($WORKERSSPECIALITY_USERID) REFERENCES $TABLE_USERS($USER_ID)," +
                "FOREIGN KEY($WORKERSSPECIALITY_SPECIALITYID) REFERENCES $TABLE_SPECIALITY($SPECIALITY_ID))"

        db?.execSQL(createUsersTable)
        db?.execSQL(createSpecialityTable)
        db?.execSQL(createWorkersSpecialityTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SPECIALITY")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_WORKERSSPECIALITY")

        onCreate(db)
    }

    fun insertUser(db: SQLiteDatabase?, users: ArrayList<UserModel>) {
        for (element in users) {
            val contentValues = ContentValues()

            contentValues.put(USER_NAME, element.name)
            contentValues.put(USER_LASTNAME, element.lastName)
            element.birthday.let { contentValues.put(USER_BIRTHDAY, element.birthday) }
            element.avatarUrl.let { contentValues.put(USER_AVATAR, element.avatarUrl) }

            db?.insert(TABLE_USERS, null, contentValues)
        }
    }

    fun insertSpeciality(db: SQLiteDatabase?, speciality: ArrayList<SpecialityModel>) {
        for (element in speciality) {
            val contentValues = ContentValues()

            contentValues.put(SPECIALITY_ID, element.id)
            contentValues.put(SPECIALITY_NAME, element.name)

            db?.insert(TABLE_SPECIALITY, null, contentValues)
        }
    }

    fun insertWorkersSpeciality(db: SQLiteDatabase?, workersSpeciality: ArrayList<WorkersSpeciality>) {
        for (element in workersSpeciality) {
            val contentValues = ContentValues()

            contentValues.put(WORKERSSPECIALITY_USERID, element.userId)
            contentValues.put(WORKERSSPECIALITY_SPECIALITYID, element.specialityId)

            db?.insert(TABLE_WORKERSSPECIALITY, null, contentValues)
        }
    }

    fun getSpecialitys(db: SQLiteDatabase?): ArrayList<SpecialityModel> {
        val cursor = db?.query(TABLE_SPECIALITY, null, null, null, null, null, null)
        val specialityModelList: ArrayList<SpecialityModel> = arrayListOf()

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex(SPECIALITY_ID)
                val nameIndex = cursor.getColumnIndex(SPECIALITY_NAME)

                do {
                    specialityModelList.add(SpecialityModel(id = cursor.getInt(idIndex), name = cursor.getString(nameIndex)))
                } while (cursor.moveToNext())
            }
        }
        cursor?.close()

        return specialityModelList
    }

    fun getWorkersFilterBySpeciality(db: SQLiteDatabase?, specialityId: Int): ArrayList<UserModel> {
        val workersSpecialityList: ArrayList<UserModel> = arrayListOf()
        val cursor = db?.rawQuery("SELECT ${TABLE_SPECIALITY}.*, ${TABLE_USERS}.*" +
                " FROM $TABLE_SPECIALITY" +
                " INNER JOIN $TABLE_WORKERSSPECIALITY" +
                " ON ${TABLE_SPECIALITY}.${SPECIALITY_ID} = ${TABLE_WORKERSSPECIALITY}.${WORKERSSPECIALITY_SPECIALITYID}" +
                " AND ${TABLE_SPECIALITY}.${SPECIALITY_ID} = $specialityId" +
                " INNER JOIN $TABLE_USERS ON ${TABLE_USERS}.${USER_ID} = ${TABLE_WORKERSSPECIALITY}.${WORKERSSPECIALITY_USERID}", null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val specialityModelList = ArrayList<SpecialityModel>()
                    specialityModelList.add(SpecialityModel(
                        id = cursor.getInt(cursor.getColumnIndex(SPECIALITY_ID)),
                        name = cursor.getString(cursor.getColumnIndex(SPECIALITY_NAME))
                    ))
                    workersSpecialityList.add(UserModel
                        (
                            id = cursor.getInt(cursor.getColumnIndex(USER_ID)),
                            name = cursor.getString(cursor.getColumnIndex(USER_NAME)),
                            lastName = cursor.getString(cursor.getColumnIndex(USER_LASTNAME)),
                            birthday = cursor.getString(cursor.getColumnIndex(USER_BIRTHDAY)),
                            avatarUrl = cursor.getString(cursor.getColumnIndex(USER_AVATAR)),
                            speciality = specialityModelList
                        )
                    )
                } while (cursor.moveToNext())
            }
        }

        cursor?.close()
        return workersSpecialityList
    }
}