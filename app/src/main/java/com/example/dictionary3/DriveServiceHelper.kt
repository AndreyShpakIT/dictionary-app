package com.example.dictionary3

import com.example.dictionary3.db.DbNames
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DriveServiceHelper(private var mDriveService: Drive) {

    private val mExecutor: Executor = Executors.newSingleThreadExecutor()

    fun createFileAsync(version: String) : Task<String> {

        return Tasks.call(mExecutor, {

            val uploadingFile: java.io.File = java.io.File(DbNames.DATABASE_PATH)

            val fileMetaData = File()
            fileMetaData.name = DbNames.DRIVE_DB_NAME
            fileMetaData.description = version

            val mediaContent = FileContent(DbNames.DATABASE_MIMETYPE, uploadingFile)

            var file: File? = null

            try {
                file = mDriveService.files().create(fileMetaData, mediaContent).execute()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            if (file.isNullOrEmpty()) {
                throw IOException("Не удалось отправить файл")
            }

            return@call file.id

        })
    }

    fun getFileAsync() : Task<File> {

        return Tasks.call(mExecutor, {
            return@call getFile() ?: throw IOException("Не удалось отправить файл")
        })

    }

    fun updateFileAsync() : Task<File> {

        return Tasks.call(mExecutor, {

            // TODO("Проверить количество локальных изменений")

            // Получить файл
            val driveFile = getFile() ?: throw IOException("Не удалось обновить файл: getFile() = null")

            val id = driveFile.id ?: throw IOException("Не удалось обновить файл: id = null")

            val driveVersion = Version(driveFile.description ?: throw IOException("Не удалось обновить файл: version = null"))
            val localVersion = Version.getLocalVersion()
            val newVersion = Version.getNowVersion()

            if (localVersion < driveVersion){
                TODO("Необходимо обноление")
            }



            // Имя и версия файла
            val fileMetaData = File()
            fileMetaData.name = DbNames.DRIVE_DB_NAME
            //fileMetaData.description = version

            // Новый файл
            val uploadingFile: java.io.File = java.io.File(DbNames.DATABASE_PATH)
            val mediaContent = FileContent(DbNames.DATABASE_MIMETYPE, uploadingFile)
            var message = "null"
            try {
                // Обновить файл
                val file = mDriveService.files().update(id, fileMetaData, mediaContent).execute()

                if (!file.isNullOrEmpty())
                    return@call file
            }
            catch (e: Exception) {
                message = e.message ?: "exception empty"
            }
            throw IOException("Не удалось обновить файл: $message")
        })

    }

    fun downloadFileAsync() : Task<Unit> {
        return Tasks.call(mExecutor, {

            // Получить файл
            val driveFile = getFile() ?: throw IOException("Не удалось скачать файл: getFile() = null")
            val id = driveFile.id ?: throw IOException("Не удалось скачать файл: id = null")

            val driveVersion = Version(driveFile.description ?: throw IOException("Не удалось скачать файл: version = null"))
            val localVersion = Version.getLocalVersion()

            if (localVersion >= driveVersion) {
                throw Exception("Последняя версия уже установлена")
            }

            try {
                // Скачать файл

                mDriveService.files().get(id).executeMediaAndDownloadTo(FileOutputStream(DbNames.DATABASE_PATH))
                //java.io.File(DbNames.DATABASE_TEMP_PATH).copyTo(java.io.File(DbNames.DATABASE_PATH), true)

                Version.setLocalVersion(driveVersion)

            } catch (e: Exception) {
                throw IOException(e.message ?: "exception empty")
            }
        })
    }

    fun getDriveVersionAsync() : Task<Version> {
        return Tasks.call(mExecutor, {
            TODO("Реализовать getDriveVersion")
            throw IOException("Не реализовано")
        })
    }

    private fun getFile() : File? {

        try {

            val listRequest = mDriveService.files().list()

            listRequest.q = "mimeType = '${DbNames.DATABASE_MIMETYPE}' and name = '${DbNames.DRIVE_DB_NAME}' and trashed = False"
            listRequest.pageSize = 1
            listRequest.fields = "nextPageToken, files(id, name, trashed, description, fileExtension)"

            val files = listRequest.execute().files

            if (files != null && files.count() > 0) {
                return files.first()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}