package org.byteskript.io;

import org.byteskript.skript.runtime.Skript;
import org.junit.Test;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class IOTest {
    
    private static final Skript skript = new Skript();
    
    @Test
    public void all() throws Throwable {
        final URI uri = IOTest.class.getClassLoader().getResource("tests").toURI();
        final Path path;
        if (uri.getScheme().equals("jar")) {
            final FileSystem system = FileSystems.newFileSystem(uri, Collections.emptyMap());
            path = system.getPath("tests");
        } else {
            path = Paths.get(uri);
        }
        final Skript.Test test = skript.new Test(true);
        test.testDirectory(path);
        final int failure = test.getFailureCount();
        for (final Throwable error : test.getErrors())
            synchronized (this) {
                error.printStackTrace(System.err);
            }
        assert failure < 1 : failure + " tests have failed.";
    }
    
}
