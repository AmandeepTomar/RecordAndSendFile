# RecordAndSendFile
In this project , We record the file and save it . after we upload the file on server using retrofit.
This a simple sample on how to upload the audio file to server using Retrofit. as we know retrofit provide us a easy to hit the apis and 
upload the data on server. 
I have just use three button in sample app 
1. For start recording the file 
2. for stop and get the saved file 
3. upload the file on server. 

As i have created a sample project so don't put url for upload file here. 

You can add your base user and file upload url and use the sample to upload the file. 

In this project we have not manage the runtime permission for M so this project will be crased in M or above. Run time permisiion code will be added soon. 

Pleas add Base URL in 
1. public class APIClient {


    public static Retrofit retrofit = null;
    // please add the base url here
    public static final String BASE_URL = "";
    }
    
  2. Add the file upload url in 
  
  public interface APIInterface {
    // you need to put your file upload url here
    @POST("")
    Call<ModelUpload> uploadTextAndAudio(
            @Body MultipartBody multipartBody
    );

}

