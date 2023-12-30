package com.cmpt370.timetracker;
import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Objects;

/**
 DataSave Class

 Responsible for serializing (serialize()) and dersializing (deserialize()) data
 IMPORTANT NOTES:

 serialize function overwrites all data in an existing file, does not append or add to it
    this means that if we have multiple classes to be saved, need to saved in a array based structure

 deserialize function pulls all data from a file in only one type of data type
    this means that similar objects should be saved in the same files, do not save different objects in the same file
 **/
public class DataSave {
    /**
     *
     * @param data - data to be saved
     * @param filePath - file name to be saved to (ending in ".ser")
     * serializes data to a file
     */
    public static void serialize(Object data, String filePath) {

        //invalid file path error checks
        if (!(filePath.contains(".ser"))) {throw new IllegalArgumentException(("invalid filepath")); }
        if (filePath == null) {throw new IllegalArgumentException(("null filepath")); }

        //write to file using object output stream
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(data);

        } catch (IOException e) {
            e.printStackTrace();   //print a detailed explanation of the error caught
        }
    }

    /**
     *
     * @param filePath - file to pull data from
     * @return - pulled data from file
     * @param <T> - adapt to object pulled and return said object
     * @throws NoSuchFileException - error case
     */
    public static <T> T deserialize(String filePath) throws NoSuchFileException {

        //invalid file path checks
        if (!(filePath.contains(".ser"))) {throw new IllegalArgumentException(("invalid filepath")); }
        if (filePath == null) {throw new IllegalArgumentException(("null filepath")); }

        //check if the file exists to pull data from
        File check = new File(filePath);
        if(!(check.exists() && !check.isDirectory())) { throw new NoSuchFileException("file not found in directory, or invalid filepath"); }

        //grab data using file input stream
        T data = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            data = (T) objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();   //print a detailed explanation of the error caught
        }

        return data;   //return given data pulled from file
    }

    public static void main(String[] args) throws NoSuchFileException {

        //check invalid filepath of serialize
        try { serialize(1, "test"); }
        catch (IllegalArgumentException e) {}

        //check null filepath of serialize
        try { serialize(1, ""); }
        catch (IllegalArgumentException e) {}

        //test proper serialization and deserialization
        try {
            serialize("testser", "testing.ser");
            String obj = deserialize("testing.ser");
            if (!(Objects.equals(obj, "testser"))) { System.out.println("error, test object not same as file object"); }
        }
        catch (Exception e) { System.out.println("exception caught"); }

        //test null filepath of deserialize
        try { deserialize(""); }
        catch (IllegalArgumentException | NoSuchFileException e) {}

        //test invalid filepath of deserialize
        try { deserialize("test"); }
        catch (IllegalArgumentException | NoSuchFileException e) {}

        //test non existent file of deserialize
        try { deserialize("opas.ser"); }
        catch (NoSuchFileException e) {}
    }
}

