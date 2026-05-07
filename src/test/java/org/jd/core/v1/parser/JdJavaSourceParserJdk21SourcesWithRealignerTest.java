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

/**
 * Test parsing JDK 21 source files to ensure compatibility with JDK 21 language features.
 *
 * JDK 21 introduces:
 * - Unnamed Patterns and Variables (JEP 443) - using _ as a placeholder
 * - Pattern Matching for switch (finalized from preview)
 * - Record Patterns (finalized from preview)
 * - String Templates (Preview - JEP 430) - not supported yet
 * - Unnamed Classes and Instance Main Methods (Preview - JEP 445) - not supported yet
 */
public class JdJavaSourceParserJdk21SourcesWithRealignerTest {

    @Test
    public void parsesAndRealignsAndReparsesAllJdk21SourcesWithoutExceptions() throws IOException {
        Path sourcesZip = locateJdk21SourcesZip();

        // Skip test if JDK 21 sources are not available
        if (sourcesZip == null) {
            System.out.println("JDK 21 sources not found. Skipping test. Set -Djdk21.src.zip to test with JDK 21 sources.");
            return;
        }

        ParserRealigner realigner = new ParserRealigner();

        int parsedFiles = 0;
        int realignedFiles = 0;
        int reparsedFiles = 0;
        int skippedFiles = 0;

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

                // Skip preview feature files that use unsupported syntax
                if (shouldSkipFile(name)) {
                    skippedFiles++;
                    continue;
                }

                String source;
                try (InputStream raw = zipFile.getInputStream(entry);
                     InputStream in = new BufferedInputStream(raw)) {
                    source = IOUtils.toString(in, StandardCharsets.UTF_8);
                }

                try {
                    System.out.print("Processing JDK 21: " + name + "...");
                    JavaParseResult parsed = JdJavaSourceParser.parse(source);
                    parsedFiles++;

                    String realigned = realigner.format(parsed);
                    realignedFiles++;

                    JdJavaSourceParser.parse(realigned);
                    reparsedFiles++;
                    System.out.println("DONE (" + reparsedFiles + ")");
                } catch (ParseException | RuntimeException ex) {
                    throw buildAssertionErrorWithAnnotatedSource(sourcesZip, name, source, ex);
                }
            }
        }

        System.out.println("JDK 21 Test Summary:");
        System.out.println("  Parsed files: " + parsedFiles);
        System.out.println("  Skipped files: " + skippedFiles);

        Assert.assertTrue(
                "At least some .java files should have been found in JDK 21 sources at: " + sourcesZip,
                parsedFiles > 0 || skippedFiles > 0
        );
        Assert.assertEquals("All parsed files should have been realigned.", parsedFiles, realignedFiles);
        Assert.assertEquals("All realigned files should have been reparsed.", parsedFiles, reparsedFiles);
    }

    /**
     * Skip files that use preview features not yet supported by the parser.
     */
    private boolean shouldSkipFile(String name) {
        // Skip files that use string templates (preview feature)
        if (name.contains("StringTemplate")) {
            return true;
        }
        // Skip files that might use unnamed classes (preview feature)
        if (name.contains("UnnamedClass")) {
            return true;
        }
        return false;
    }

    private static AssertionError buildAssertionErrorWithAnnotatedSource(
            Path sourcesZip,
            String entryName,
            String source,
            Throwable ex
    ) {
        String header = "Processing JDK 21 failed for entry '" + entryName + "' in sources archive: " + sourcesZip
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

    private static Path locateJdk21SourcesZip() {
        // First try explicit property
        Path explicit = safePath(System.getProperty("jdk21.src.zip"));
        if (explicit != null && Files.isRegularFile(explicit)) {
            return explicit;
        }

        // Try common locations
        Path[] candidates = new Path[]{
                safePath("/usr/lib/jvm/java-21-openjdk-amd64/lib/src.zip"),
                safePath("/usr/lib/jvm/java-21/lib/src.zip"),
                safePath(System.getenv("JAVA_21_HOME") + "/lib/src.zip"),
        };

        for (Path candidate : candidates) {
            if (candidate != null && Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        // Return null to allow test to be skipped
        return null;
    }

    private static Path safePath(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return Paths.get(trimmed);
        } catch (Exception e) {
            return null;
        }
    }
}
