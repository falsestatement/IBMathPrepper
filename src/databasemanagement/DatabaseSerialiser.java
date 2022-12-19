package databasemanagement;

import java.io.*;
import java.util.ArrayList;

public class DatabaseSerialiser<T>  {
    private ArrayList<T> objects;
    private String filename;
    private String extension = ".ibmhl";

    public DatabaseSerialiser(String filename, ArrayList<T> objects){
        this.objects = objects;
        this.filename = filename + extension;
    }

    public DatabaseSerialiser(String filename){
        this.filename = filename + extension;
    }

    public void serialize() {
        // check if there are objects to serialize
        if (objects != null) {
            // Init output streams
            FileOutputStream fileOutputStream = null;
            ObjectOutputStream objectOutputStream = null;

            // saving to file
            try {
                // setting up output streams
                fileOutputStream = new FileOutputStream(filename);
                objectOutputStream = new ObjectOutputStream(fileOutputStream);

                // saving object
                objectOutputStream.writeObject(objects);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // terminating output streams
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            System.err.println("Unable to serialise objects: No objects declared");
        }
    }

    public ArrayList<T> deserialise(){
        // init input streams and result array
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        // try to access file
        try{
            // setting up input streams
            fileInputStream = new FileInputStream(filename);
            objectInputStream = new ObjectInputStream(fileInputStream);

            // returning stored array
            return (ArrayList<T>)objectInputStream.readObject();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            // terminating input streams
            if(fileInputStream != null){
                try{
                    fileInputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(objectInputStream != null){
                try{
                    objectInputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean exists(){
        return new File(filename).exists();
    }

    public void delete(){
        new File(filename).delete();
    }
}
