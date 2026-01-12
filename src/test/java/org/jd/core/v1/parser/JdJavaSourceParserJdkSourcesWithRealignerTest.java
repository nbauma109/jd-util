package org.jd.core.v1.parser;

import org.apache.commons.io.IOUtils;
import org.jd.core.v1.util.ParserRealigner;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JdJavaSourceParserJdkSourcesWithRealignerTest {

    @Test
    public void parsesAndRealignsAndReparsesAllJavaDevelopmentKitSourcesWithoutExceptions() throws IOException {
        Path sourcesZip = locateJavaDevelopmentKitSourcesZip();
        ParserRealigner realigner = new ParserRealigner();

        int parsedFiles = 0;
        int realignedFiles = 0;
        int reparsedFiles = 0;

        try (ZipFile zipFile = new ZipFile(sourcesZip.toFile(), StandardCharsets.UTF_8)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();
                if (!name.endsWith(".java")) {
                    continue;
                }

                String source;
                try (InputStream raw = zipFile.getInputStream(entry);
                     InputStream in = new BufferedInputStream(raw)) {
                    source = IOUtils.toString(in, StandardCharsets.UTF_8);
                }

                try {
                    System.out.print("Processing " + name + "...");
                    JavaParseResult parsed = JdJavaSourceParser.parse(source);
                    parsedFiles++;

                    String realigned = realigner.format(parsed);
                    realignedFiles++;

                    JdJavaSourceParser.parse(realigned);
                    reparsedFiles++;
                    System.out.println("DONE (" + reparsedFiles + ")");
                } catch (ParseException ex) {
                    throw buildAssertionErrorWithAnnotatedSource(sourcesZip, name, source, ex);
                } catch (RuntimeException ex) {
                    throw buildAssertionErrorWithAnnotatedSource(sourcesZip, name, source, ex);
                }
            }
        }

        Assert.assertTrue(
                "No .java files were found in the Java Development Kit (JDK) sources archive at: " + sourcesZip,
                parsedFiles > 0
        );
        Assert.assertEquals("All parsed files should have been realigned.", parsedFiles, realignedFiles);
        Assert.assertEquals("All realigned files should have been reparsed.", parsedFiles, reparsedFiles);
    }

    private static AssertionError buildAssertionErrorWithAnnotatedSource(
            Path sourcesZip,
            String entryName,
            String source,
            Throwable ex
    ) {
        String header = "Processing failed for entry '" + entryName + "' in sources archive: " + sourcesZip
                + System.lineSeparator()
                + "Failure: " + safeMessage(ex);

        String annotated = header + System.lineSeparator() + System.lineSeparator()
                + "----- BEGIN INPUT (annotated) -----" + System.lineSeparator()
                + toBlockCommentLineNumbered(source)
                + System.lineSeparator() + "----- END INPUT (annotated) -----";

        AssertionError ae = new AssertionError(annotated);
        ae.initCause(ex);
        return ae;
    }

    private static String safeMessage(Throwable ex) {
        String message = ex.getMessage();
        if (message == null) {
            return ex.getClass().getName();
        }
        String trimmed = message.trim();
        return trimmed.isEmpty() ? ex.getClass().getName() : trimmed;
    }

    private static String toBlockCommentLineNumbered(String source) {
        if (source == null) {
            return "";
        }

        String normalized = normalizeToLf(source);
        String[] lines = normalized.split("\n", -1);

        StringBuilder sb = new StringBuilder(normalized.length() + (lines.length * 16));
        for (int i = 0; i < lines.length; i++) {
            int lineNumber = i + 1;
            sb.append("/* ").append(lineNumber).append(" */ ").append(lines[i]).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static String normalizeToLf(String s) {
        if (s.indexOf('\r') < 0) {
            return s;
        }
        String withoutCrLf = s.replace("\r\n", "\n");
        return withoutCrLf.replace('\r', '\n');
    }

    private static Path locateJavaDevelopmentKitSourcesZip() {
        Path explicit = safePath(System.getProperty("jdk.src.zip"));
        if (explicit != null && Files.isRegularFile(explicit)) {
            return explicit;
        }

        Path[] candidates = buildSourcesZipCandidates();
        for (Path candidate : candidates) {
            if (candidate != null && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Could not find the Java Development Kit (JDK) sources archive (src.zip).").append(System.lineSeparator());
        sb.append("Searched locations derived from java.home and JAVA_HOME, plus optional -Djdk.src.zip.").append(System.lineSeparator());
        sb.append("java.home=").append(System.getProperty("java.home")).append(System.lineSeparator());
        sb.append("JAVA_HOME=").append(System.getenv("JAVA_HOME")).append(System.lineSeparator());
        sb.append("jdk.src.zip=").append(System.getProperty("jdk.src.zip"));
        throw new AssertionError(sb.toString());
    }

    private static Path[] buildSourcesZipCandidates() {
        Path javaHome = safePath(System.getProperty("java.home"));
        Path envJavaHome = safePath(System.getenv("JAVA_HOME"));

        Path javaHomeParent = (javaHome == null) ? null : javaHome.getParent();
        Path javaHomeGrandParent = (javaHomeParent == null) ? null : javaHomeParent.getParent();

        return new Path[]{
                resolve(javaHome, "lib", "src.zip"),
                resolve(javaHomeParent, "lib", "src.zip"),
                resolve(envJavaHome, "lib", "src.zip"),

                resolve(javaHome, "src.zip"),
                resolve(javaHomeParent, "src.zip"),
                resolve(javaHomeGrandParent, "src.zip"),
                resolve(envJavaHome, "src.zip")
        };
    }

    private static Path resolve(Path base, String first, String second) {
        if (base == null) {
            return null;
        }
        return base.resolve(first).resolve(second);
    }

    private static Path resolve(Path base, String single) {
        if (base == null) {
            return null;
        }
        return base.resolve(single);
    }

    private static Path safePath(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return Paths.get(trimmed);
    }
}
