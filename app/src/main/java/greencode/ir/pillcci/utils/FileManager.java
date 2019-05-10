package greencode.ir.pillcci.utils;

import java.io.File;

/**
 * Created by alireza on 5/23/2017.
 */

public class FileManager {
    public static File makeFile(){

            if(!Constants.Documents.exists()){
                Constants.Documents.mkdirs();
            }
            return Constants.Documents;


    }



}
