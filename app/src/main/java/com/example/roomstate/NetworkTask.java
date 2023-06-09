package com.example.roomstate;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

public class NetworkTask extends AsyncTask<String, Void, String> {

    public Function<String, Void> dataHandler;

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String synxCatHeader = params[1];
        String data = params[2];
        String response = "";
        System.out.println("Trying to connect Network task");

        try {
            // Create a new HTTP connection
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set the request method to POST
            con.setRequestMethod("POST");

            // Set the Synx-Cat header
            con.setRequestProperty("Synx-Cat", synxCatHeader);

            // Set the data
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(data);
            writer.flush();
            System.out.println("Network task connected");

            // Read the response as a stream
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
                if (dataHandler != null && line.length() > 0) {
                    dataHandler.apply(line);
                }
                System.out.println(line);

                if (isCancelled()) {
                    break;
                }
            }

            // Close the stream
            reader.close();
            System.out.println("Network task closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        // Process the response
        System.out.println(result);
    }
}
