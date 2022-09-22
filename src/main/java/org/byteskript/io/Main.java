package org.byteskript.io;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.language.PostCompileClass;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.runtime.Skript;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class Main extends ModifiableLibrary {
    public static final Main LIBRARY = new Main();
    public static final Type BYTE = new Type(Byte.class),
        INPUT = new Type(InputStream.class),
        OUTPUT = new Type(OutputStream.class);
    
    public Main() {
        super("io");
        this.registerTypes(BYTE, INPUT, OUTPUT);
        this.registerConverter(Byte.class, Integer.class, Byte::intValue);
    }
    
    public static void main(String... args) {
        System.out.println("This goes in the ByteSkript libraries/ folder.");
    }
    
    public static void load(Skript skript) {
        skript.registerLibrary(LIBRARY);
    }
    
    @Override
    public Collection<PostCompileClass> getRuntime() {
        try {
            return List.of(this.getData(Class.forName("io")));
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private PostCompileClass getData(final Class<?> type) throws IOException {
        return new PostCompileClass(getSource(type), type.getName(), new Type(type).internalName());
    }
    
    private byte[] getSource(final Class<?> cls) throws IOException {
        try (final InputStream stream = ClassLoader.getSystemResourceAsStream(cls.getName()
            .replace('.', '/') + ".class")) {
            assert stream != null;
            return stream.readAllBytes();
        }
    }
}
