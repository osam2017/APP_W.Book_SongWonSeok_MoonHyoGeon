package com.example.administrator.wbook;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sws28 on 2017-10-25.
 */

public class AndroidUploader {
    static String postUrl = RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/regibookimg";
    static String CRLF = "\r\n";
    static String twoHyphens = "--";
    static String boundary = "*****b*o*u*n*d*a*r*y*****";

    private String pictureFileName = null;
    private String title = null;
    private String isbn = null;
    private DataOutputStream dataStream = null;

    enum ReturnCode { noPicture, unknown, http201, http400, http401, http403, http404, http500};

    private String TAG = "멀티파트 테스트";

    public AndroidUploader(String title, String isbn)         {
        this.title = title;
        this.isbn = isbn;
    }

    public ReturnCode uploadPicture(String pictureFileName)        {
        this.pictureFileName = pictureFileName;
        File uploadFile = new File(pictureFileName);

        if (uploadFile.exists())
            try     {
                FileInputStream fileInputStream = new FileInputStream(uploadFile);
                URL connectURL = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");

                //conn.setRequestProperty("User-Agent", "myFileUploader");
                conn.setRequestProperty("Connection","Keep-Alive");
                conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);

                conn.connect();

                dataStream = new DataOutputStream(conn.getOutputStream());

                writeFormField("title", title);
                writeFormField("isbn", isbn);
                writeFileField("imageurl", uploadFile.getName(), "image/jpg", fileInputStream);

                // final closing boundary line
                dataStream.writeBytes(twoHyphens + boundary + twoHyphens + CRLF);

                fileInputStream.close();
                dataStream.flush();
                dataStream.close();
                dataStream = null;

                String response = getResponse(conn);
                int responseCode = conn.getResponseCode();

                if (response.contains("uploaded successfully"))
                    return ReturnCode.http201;
                else
                    // for now assume bad name/password
                    return ReturnCode.http401;
            }
            catch (MalformedURLException mue) {
                Log.e(TAG, "error: " + mue.getMessage(), mue);
                return ReturnCode.http400;
            }
            catch (IOException ioe) {
                Log.e(TAG, "error: " + ioe.getMessage(), ioe);
                return ReturnCode.http500;
            }
            catch (Exception e) {
                Log.e(TAG, "error: " + e.getMessage(), e);
                return ReturnCode.unknown;
            }    else    {
            return ReturnCode.noPicture;
        }
    }

    private String getResponse(HttpURLConnection conn)        {
        try             {
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            byte []        data = new byte[1024];
            int             len = dis.read(data, 0, 1024);

            dis.close();
            int responseCode = conn.getResponseCode();

            if (len > 0)
                return new String(data, 0, len);
            else
                return "";
        }
        catch(Exception e)     {
            //System.out.println("AndroidUploader: "+e);
            Log.e(TAG, "AndroidUploader: "+e);
            return "";
        }
    }

    /**
     *  this mode of reading response no good either
     */
    private String getResponseOrig(HttpURLConnection conn)        {
        InputStream is = null;
        try   {
            is = conn.getInputStream();
            // scoop up the reply from the server
            int ch;
            StringBuffer sb = new StringBuffer();
            while( ( ch = is.read() ) != -1 ) {
                sb.append( (char)ch );
            }
            return sb.toString();   // TODO Auto-generated method stub
        }
        catch(Exception e)   {
            //System.out.println("GeoPictureUploader: biffed it getting HTTPResponse");
            Log.e(TAG, "AndroidUploader: "+e);
        }
        finally   {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {}
        }

        return "";
    }

    /**
     * write one form field to dataSream
     * @param fieldName
     * @param fieldValue
     */
    private void writeFormField(String fieldName, String fieldValue)  {
        try  {
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"" + CRLF);
            dataStream.writeBytes(CRLF);
            dataStream.writeBytes(fieldValue);
            dataStream.writeBytes(CRLF);
        }    catch(Exception e)   {
            Log.e(TAG, "AndroidUploader.writeFormField: " + e.getMessage());
        }
    }

    /**
     * write one file field to dataSream
     * @param fieldName - name of file field
     * @param fieldValue - file name
     * @param type - mime type
     */
    private void writeFileField(String fieldName, String fieldValue, String type, FileInputStream fis)  {
        try {
            // opening boundary line
            dataStream.writeBytes(twoHyphens + boundary + CRLF);
            dataStream.writeBytes("Content-Disposition: form-data; name=\""
                    + fieldName
                    + "\";filename=\""
                    + fieldValue
                    + "\""
                    + CRLF);
            Log.e(TAG, "AndroidUploader: "+dataStream.toString());
            dataStream.writeBytes("Content-Type: " + type +  CRLF);
            dataStream.writeBytes(CRLF);

            // create a buffer of maximum size
            int bytesAvailable = fis.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fis.read(buffer, 0, bufferSize);
            while (bytesRead > 0)   {
                dataStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
            }

            // closing CRLF
            dataStream.writeBytes(CRLF);
        }
        catch(Exception e)  {
            Log.e(TAG, "AndroidUploader.writeFormField: got: " + e.getMessage());
        }
    }
}
