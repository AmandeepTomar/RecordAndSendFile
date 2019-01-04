package com.amandeep.recordandsendfile;

import android.media.MediaRecorder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amandeep.recordandsendfile.retrofit.APIClient;
import com.amandeep.recordandsendfile.retrofit.APIInterface;
import com.amandeep.recordandsendfile.utills.FileStorageutills;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.amandeep.recordandsendfile.utills.FileStorageutills.AUDIO_FOLDER_NAME;

public class MainActivity extends AppCompatActivity {
    private TextView tvFilePath;
    private Button btnStartRecording, btnStopNSaveRecording, btnUploadFIle, btnPlayFIle, btnStopFile;
    APIInterface apiInterface;
    private File file;
    MediaRecorder mRecorder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFilePath = findViewById(R.id.tvFilePath);
        btnStartRecording = findViewById(R.id.btnStartRecording);
        btnStopNSaveRecording = findViewById(R.id.btnStopRecording);
        btnUploadFIle = findViewById(R.id.btnUploaFile);

        apiInterface = APIClient.getClient(this).create(APIInterface.class);

        btnStartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        btnStopNSaveRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecordingAndSaveFileHere();
            }
        });

        btnUploadFIle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filePath = tvFilePath.getText().toString();
                uploadFileToServer(filePath);
            }
        });

    }

    // start Recording here
    private void startRecording() {
        if (FileStorageutills.isExternalStorageWritable()) {
            String createdTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            file = FileStorageutills.getNewRootDirectoryAudio(createdTime + "myrecord");
            if (file != null) {
                startRecordingWithFile(file);
            }
        }
    }

    private void startRecordingWithFile(File file) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
        } else
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(file.getAbsolutePath());
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("data", "prepare() failed" + e.toString());
        }
        mRecorder.start();
    }


    private void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }


    // this method is used to stop recording and save file
    private void stopRecordingAndSaveFileHere() {
        stopRecording();

        String path = FileStorageutills.getRootFolderOFTruxAi().getAbsolutePath();
        String path2 = FileStorageutills.getAudioFileFOlder().getAbsolutePath();
        Log.e("data here", path + " \n" + path2);


        List<File> list = FileStorageutills.fetchDataFromFolders(AUDIO_FOLDER_NAME);
        String filePath = null;
        if (list != null) {
            for (File file : list) {
                Log.e(file.getName(), file.getAbsolutePath());
                tvFilePath.setText(file.getAbsolutePath());

                filePath = file.getAbsolutePath();
                Log.e("gett file path", filePath);
            }
        }
        if (filePath != null)
            tvFilePath.setText(filePath);
    }

    // this method is used to upload the file to server using retrofit
    private void uploadFileToServer(final String filePath) {
        Log.e("file path in api", filePath);
        File audioFile = new File(filePath);
        final MultipartBody requestBody;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        RequestBody audiobody = RequestBody.create(MediaType.parse("audio/*"), audioFile);
        builder.addFormDataPart("bill", audioFile.getName(), audiobody);
        requestBody = builder.build();

        apiInterface.uploadTextAndAudio(requestBody).enqueue(new Callback<ModelUpload>() {
            @Override
            public void onResponse(Call<ModelUpload> call, Response<ModelUpload> response) {
                // here we manage the result
                Log.e("here in response", response + "");
                if (response.code() == 200) {
                    Log.e("status Code", response.code() + " received");
                    boolean isDelete = FileStorageutills.deleteFile(filePath);
                    Log.e("after result", isDelete + " received");
                }
            }

            @Override
            public void onFailure(Call<ModelUpload> call, Throwable t) {
                // here we manage errors
                Log.e("on failure result", " received" + t.toString());

            }
        });
    }
}
