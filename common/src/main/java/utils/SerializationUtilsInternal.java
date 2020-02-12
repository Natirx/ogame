package utils;

import org.apache.commons.lang3.SerializationUtils;

import java.io.*;

public final class SerializationUtilsInternal {

    public static <T extends Serializable> void serialize(T object, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            SerializationUtils.serialize(object, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't find a file " + file.getAbsolutePath());
        }
    }

    public static <T> T deserialize(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            return SerializationUtils.deserialize(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't find a file " + file.getAbsolutePath());
        }
    }
}
