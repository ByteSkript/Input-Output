import mx.kenzie.jupiter.stream.InputStreamController;
import mx.kenzie.jupiter.stream.Stream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * This is the Java implementation of the 'io' namespace functions.
 * Some implementations may re-implement this in Skript itself.
 * <p>
 * The unusual casing is to match Skript's code-style.
 */
public final class io {
    
    public static OutputStream byte_output_stream() {
        return new ByteArrayOutputStream();
    }
    
    public static OutputStream open_output_stream(Object object) throws IOException {
        if (object == null) return null;
        if (object instanceof OutputStream stream) return stream;
        if (object instanceof File file) return new FileOutputStream(file);
        return null;
    }
    
    public static Iterable<Byte> loop_bytes(Object object) throws IOException {
        if (object instanceof InputStreamController controller) return controller;
        if (object instanceof InputStream stream) return Stream.controller(stream);
        if (object == null) return () -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }
            
            @Override
            public Byte next() {
                return -1;
            }
        };
        return Stream.controller(open_input_stream(object));
    }
    
    public static InputStream open_input_stream(Object object) throws IOException {
        if (object == null) return null;
        if (object instanceof InputStreamController stream) return stream;
        if (object instanceof InputStream stream) return stream;
        if (object instanceof File file) return new FileInputStream(file);
        if (object instanceof String string) return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        return new ByteArrayInputStream(new byte[0]);
    }
    
    public static void write(OutputStream stream, Number number) throws IOException {
        if (number instanceof Long) {
            final long value = number.longValue();
            final byte[] buffer = new byte[8];
            buffer[0] = (byte) (value >>> 56);
            buffer[1] = (byte) (value >>> 48);
            buffer[2] = (byte) (value >>> 40);
            buffer[3] = (byte) (value >>> 32);
            buffer[4] = (byte) (value >>> 24);
            buffer[5] = (byte) (value >>> 16);
            buffer[6] = (byte) (value >>> 8);
            buffer[7] = (byte) (value);
            stream.write(buffer);
        } else if (number instanceof Integer) {
            final int value = number.intValue();
            final byte[] buffer = new byte[4];
            buffer[0] = (byte) (value >>> 24);
            buffer[1] = (byte) (value >>> 16);
            buffer[2] = (byte) (value >>> 8);
            buffer[3] = (byte) (value);
            stream.write(buffer);
        } else stream.write(number.byteValue());
    }
    
    public static void close(Object object) throws Throwable {
        if (object instanceof InputStreamController controller) controller.close();
        if (object instanceof AutoCloseable closeable) closeable.close();
    }
    
    public static byte read_byte(InputStream stream) throws IOException {
        return (byte) stream.read();
    }
    
    public static String read_character(InputStream stream) throws IOException {
        final int first = stream.read(), second = stream.read();
        if (second < 0) throw new EOFException();
        return "" + (char) ((first << 8) + second);
    }
    
    public static int read_integer(InputStream stream) throws IOException {
        final int a = stream.read(), b = stream.read(), c = stream.read(), d = stream.read();
        if (d < 0) throw new EOFException();
        return (a << 24) + (b << 16) + (c << 8) + d;
    }
    
    public static String read_string(InputStream stream, Number length) throws IOException {
        final int amount = length.intValue();
        final byte[] bytes = new byte[amount];
        final int count = stream.read(bytes);
        assert count == amount;
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
}
