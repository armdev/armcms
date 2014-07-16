package frontend.web.utils;

/**
 *
 * @author Armen Arzumanyan
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public final class UploadUtil {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private UploadUtil() {
    }

    public static void write(File file, byte[] bytes) throws IOException {
        write(file, new ByteArrayInputStream(bytes), false);
    }

    public static void write(File file, byte[] bytes, boolean append) throws IOException {
        write(file, new ByteArrayInputStream(bytes), append);
    }

    public static void write(File file, InputStream input) throws IOException {
        write(file, input, false);
    }

    public static void write(File file, InputStream input, boolean append) throws IOException {
        mkdirs(file);
        BufferedOutputStream output = null;

        try {
            output = new BufferedOutputStream(new FileOutputStream(file, append));
            int data = -1;
            while ((data = input.read()) != -1) {
                output.write(data);
            }
        } finally {
            close(input, file);
            close(output, file);
        }
    }

    public static void write(File file, char[] chars) throws IOException {
        write(file, new CharArrayReader(chars), false);
    }

    public static void write(File file, char[] chars, boolean append) throws IOException {
        write(file, new CharArrayReader(chars), append);
    }

    public static void write(File file, String string) throws IOException {
        write(file, new CharArrayReader(string.toCharArray()), false);
    }

    public static void write(File file, String string, boolean append) throws IOException {
        write(file, new CharArrayReader(string.toCharArray()), append);
    }

    public static void write(File file, Reader reader) throws IOException {
        write(file, reader, false);
    }

    public static void write(File file, Reader reader, boolean append) throws IOException {
        mkdirs(file);
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            int data = -1;
            while ((data = reader.read()) != -1) {
                writer.write(data);
            }
        } finally {
            close(reader, file);
            close(writer, file);
        }
    }

    public static void write(File file, List<String> records) throws IOException {
        write(file, records, false);
    }

    public static void write(File file, List<String> records, boolean append) throws IOException {
        mkdirs(file);
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file, append));
            for (String record : records) {
                writer.write(record);
                writer.write(LINE_SEPARATOR);
            }
        } finally {
            close(writer, file);
        }
    }

    public static byte[] readBytes(File file) throws IOException {
        BufferedInputStream stream = (BufferedInputStream) readStream(file);
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        return bytes;
    }

    public static InputStream readStream(File file) throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public static char[] readChars(File file) throws IOException {
        BufferedReader reader = (BufferedReader) readReader(file);
        char[] chars = new char[(int) file.length()];
        reader.read(chars);
        return chars;
    }

    public static String readString(File file) throws IOException {
        return new String(readChars(file));
    }

    public static Reader readReader(File file) throws IOException {
        return new BufferedReader(new FileReader(file));
    }

    public static List<String> readRecords(File file) throws IOException {
        BufferedReader reader = (BufferedReader) readReader(file);
        List<String> records = new ArrayList<String>();
        String record = null;

        try {
            while ((record = reader.readLine()) != null) {
                records.add(record);
            }
        } finally {
            close(reader, file);
        }

        return records;
    }

    public static void copy(File source, File destination) throws IOException {
        copy(source, destination, true);
    }

    public static void copy(File source, File destination, boolean overwrite) throws IOException {
        if (destination.exists() && !overwrite) {
            throw new IOException(
                    "Copying file " + source.getPath() + " to " + destination.getPath() + " failed."
                    + " The destination file already exists.");
        }

        mkdirs(destination);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            input = new BufferedInputStream(new FileInputStream(source));
            output = new BufferedOutputStream(new FileOutputStream(destination));
            int data = -1;
            while ((data = input.read()) != -1) {
                output.write(data);
            }
        } finally {
            close(input, source);
            close(output, destination);
        }
    }

    public static void move(File source, File destination) throws IOException {
        move(source, destination, true);
    }

    public static void move(File source, File destination, boolean overwrite) throws IOException {
        if (destination.exists()) {
            if (overwrite) {
                destination.delete();
            } else {
                throw new IOException(
                        "Moving file " + source.getPath() + " to " + destination.getPath() + " failed."
                        + " The destination file already exists.");
            }
        }

        mkdirs(destination);

        if (!source.renameTo(destination)) {
            throw new IOException(
                    "Moving file " + source.getPath() + " to " + destination.getPath() + " failed.");
        }
    }

    public static String trimFilePath(String fileName) {
        return fileName
                .substring(fileName.lastIndexOf("/") + 1)
                .substring(fileName.lastIndexOf("\\") + 1);
    }

    public static File uniqueFile(File filePath, String fileName) throws IOException {
        File file = new File(filePath, fileName);

        if (file.exists()) {

            // Split filename and add braces, e.g. "name.ext" --> "name[", "].ext".
            String prefix;
            String suffix;
            int dotIndex = fileName.lastIndexOf(".");

            if (dotIndex > -1) {
                prefix = fileName.substring(0, dotIndex) + "[";
                suffix = "]" + fileName.substring(dotIndex);
            } else {
                prefix = fileName + "[";
                suffix = "]";
            }

            int count = 0;


            while (file.exists()) {
                if (count < 0) {
                    throw new IOException("No unique filename available for " + fileName
                            + " in path " + filePath.getPath() + ".");
                }


                file = new File(filePath, prefix + (count++) + suffix);
            }
        }

        return file;
    }

    private static void mkdirs(File file) throws IOException {
        if (file.exists() && !file.isFile()) {
            throw new IOException("File " + file.getPath() + " is actually not a file.");
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IOException("Creating directories " + parentFile.getPath() + " failed.");
        }
    }

    private static void close(Closeable resource, File file) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                String message = "Closing file " + file.getPath() + " failed.";

                System.err.println(message);
            }
        }
    }
}