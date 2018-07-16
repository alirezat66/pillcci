package greencode.ir.pillcci.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import saman.zamani.persiandate.PersianDate;

/**
 * Created by alireza on 6/12/18.
 */

public class ReadAndWrite {
    public static File textDir = new File(
            Environment.getExternalStoragePublicDirectory(
                    Environment.getRootDirectory().getParent()
            ),
            "/PilCCi/log"
    );

    public static void appendLog(String text)
    {
        File logFile = new File("sdcard/log.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
       public static void writeLog(String s){
           if(!textDir.exists()){
               textDir.mkdir();
           }

           File file = new File(textDir+"/"+"log.txt");

               try {
                   FileOutputStream fileOutputStream = new FileOutputStream(file,true);
                   fileOutputStream.write((s+System.getProperty("line.separator")).getBytes());

               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               } catch (IOException e) {
                   e.printStackTrace();
               }





       }
    public static String getHMS(PersianDate date){
        return date.getHour()+":"+date.getMinute()+":"+date.getSecond();
    }



}
