package ar.edu.utn.frc.tup.lc.iv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class FileLoader {

    private FileLoader() {
    }

    public static String loadFrom(String path) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try(InputStream objJsonPath = classLoader.getResourceAsStream(path)) {
                String space = " ";
                assert objJsonPath != null;
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(objJsonPath))) {
                    line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append(space);
                        line = reader.readLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("FileLoader.loadFrom(String) failed.", e);
        }
        return stringBuilder.toString();
    }
}
