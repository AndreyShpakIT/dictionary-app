package com.example.dictionary3

import com.example.dictionary3.db.DbNames
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DriveServiceHelper(private var mDriveService: Drive) {

    private val mimeType: String = "application/vnd.sqlite3"
    private val driveFileName: String = "AndroidWords.db"
    val dbFilePath: String = "/data/data/com.example.dictionary3/databases/${DbNames.DATABASE_NAME}"
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    fun createFile() : Task<String> {

        return Tasks.call(mExecutor, {

            val uploadingFile: java.io.File = java.io.File(dbFilePath)

            val fileMetaData: File = File()
            fileMetaData.name = driveFileName

            val mediaContent: FileContent = FileContent(mimeType, uploadingFile)

            var file: File? = null

            try {
                file = mDriveService.files().create(fileMetaData, mediaContent).execute()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            if (file.isNullOrEmpty()) {
                return@call null
            }

            return@call file.id

        })

    }

    private fun getFile() : File? {
        try {

            var listRequest = mDriveService.files().list()

            listRequest.q = "mimeType = '$mimeType' and name = '$driveFileName' and trashed = False"
            listRequest.pageSize = 1
            listRequest.fields = "nextPageToken, files(id, name, trashed, description, fileExtension)"

            var files = listRequest.execute().files

            if (files != null && files.count() > 0) {
                return files.first()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getFileAsync() : Task<File> {

        return Tasks.call(mExecutor, {
            return@call getFile()
        })

    }

    fun updateFile() : Task<File> {

        return Tasks.call(mExecutor, {

            // Получить файл
            val driveFile = getFile() ?: return@call null

            val id = driveFile.id ?: return@call null
            val version = driveFile.description ?: return@call null

            //TODO("Class Version. Сравнение локальной и облачной версий")
            /*val localVersion = ""
            val driveVersion = driveFile.version

            if (driveVersion > localVersion)*/

            // Имя и версия файла
            val fileMetaData = File()
            fileMetaData.name = driveFileName
            fileMetaData.description = version

            // Новый файл
            val uploadingFile: java.io.File = java.io.File(dbFilePath)
            val mediaContent = FileContent(mimeType, uploadingFile)

            try {
                // Обновить файл
                val file = mDriveService.files().update(id, fileMetaData, mediaContent).execute()

                if (!file.isNullOrEmpty())
                    return@call file
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            return@call null
        })

    }

}

class FileData (val id: String?, val version: String?) {

}