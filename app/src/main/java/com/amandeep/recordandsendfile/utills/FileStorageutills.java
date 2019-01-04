package com.amandeep.recordandsendfile.utills;

import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileStorageutills {
    public static final String LOG_TAG = "FileStorageUtils";
    private static final String FOLDER_NAME = "MyAppTest-Folder";
    public static final String AUDIO_FOLDER_NAME = "MyAppTest-Folder";

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public static File getRootFolderOFTruxAi() {
        File file = Environment.getExternalStorageDirectory();
        File file1 = new File(file.getAbsoluteFile() + "/" + FOLDER_NAME);
        if (file1.isDirectory()) {

        } else {
            file1.mkdir();
        }
        return file1;
    }


    public static File getAudioFileFOlder() {
        File dir = null;
        try {
            File file = getRootFolderOFTruxAi();
            dir = new File(file.getAbsoluteFile() + "/" + AUDIO_FOLDER_NAME);
            if (dir.isDirectory()) {

            } else {
                dir.mkdir();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dir;
    }


    public static File getNewRootDirectoryAudio(String fileName) {
        File file1 = null;
        try {
            File file = getRootFolderOFTruxAi();
            File dir = new File(file.getAbsoluteFile() + "/" + AUDIO_FOLDER_NAME);

            if (dir.isDirectory()) {
                file1 = new File(dir + "/" + "/" + fileName + ".mp3");
                Log.e("yep file cfeate", file1.getAbsolutePath() + " ");
            } else {
                dir.mkdir();
                file1 = new File(dir + "/" + "/" + fileName + ".mp3");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return file1;
    }


    public static List<File> fetchDataFromFolders(String folderName) {
        List<File> listData = null;
        File file = getRootFolderOFTruxAi();
        File file1 = null;
        File dir = new File(file.getAbsoluteFile() + "/" + folderName);
        if (dir.isDirectory()) {
            File file2[] = dir.listFiles();

            listData = Arrays.asList(file2);
        } else {
            dir.mkdir();
            listData = new ArrayList<>();
        }
        return listData;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] convertFileContentToBlobForOreo(String filePathStr)
            throws IOException {
        // get path object pointing to file
        Path filePath = Paths.get(filePathStr);
        // get byte array with file contents
        byte[] fileContent = Files.readAllBytes(filePath);
        return fileContent;
    }


    public static byte[] convertFileContentToBlobBelow26(String filePath) throws IOException {
        // create file object
        File file = new File(filePath);
        // initialize a byte array of size of the file
        byte[] fileContent = new byte[(int) file.length()];
        FileInputStream inputStream = null;
        try {
            // create an input stream pointing to the file
            inputStream = new FileInputStream(file);
            // read the contents of file into byte array
            inputStream.read(fileContent);
        } catch (IOException e) {
            throw new IOException("Unable to convert file to byte array. " + e.getMessage());
        } finally {
            // close input stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return fileContent;
    }


    public static boolean deleteFile(String mFileName) {
        File file = new File(mFileName);
        boolean isDelete = file.delete();
        return isDelete;
    }
}
