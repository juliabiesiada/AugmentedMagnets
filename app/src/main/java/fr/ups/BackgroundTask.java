package fr.ups;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask <String, Void, Void> {

    Context ctx;

    BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        String reg_url = "http://192.168.1.87/Magnets_DB/addNote.php";
        String method = params[0];
        if (method == "put") {

            String tid = params[1];
            String type = params[2];
            String text = params[3];
            String image = params[4];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream =  httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("tid", "UTF-8") + " = " + URLEncoder.encode(tid, "UTF-8") + "&" +
                        URLEncoder.encode("type", "UTF-8") + " = " + URLEncoder.encode(type, "UTF-8") + "&" +
                        URLEncoder.encode("text", "UTF-8") + " = " + URLEncoder.encode(text, "UTF-8") + "&" +
                        URLEncoder.encode("image", "UTF-8") + " = " + URLEncoder.encode(image, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate (Void... values ) {
        super.onProgressUpdate();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

