/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.util;

import org.jd.core.v1.api.loader.Loader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipLoader implements Loader {

    protected static final Pattern CLASS_SUFFIX_PATTERN = Pattern.compile("\\.class$");

    private HashMap<String, byte[]> map = new HashMap<String, byte[]>();

    public  ZipLoader(InputStream in) throws IOException {
        byte[] buffer = new byte[1024 * 2];

            try (ZipInputStream zis = new ZipInputStream(in)) {
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {
                    if (!ze.isDirectory()) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        int read = zis.read(buffer);

                        while (read > 0) {
                            out.write(buffer, 0, read);
                            read = zis.read(buffer);
                        }

                        map.put(makeEntryName(ze.getName()), out.toByteArray());
                    }

                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
            }
    }

    protected String makeEntryName(String entryName) {
        return CLASS_SUFFIX_PATTERN.matcher(entryName).replaceFirst("");
    }

    @Override
    public byte[] load(String internalName) throws IOException {
        return map.get(makeEntryName(internalName));
    }

    @Override
    public boolean canLoad(String internalName) {
        return map.containsKey(makeEntryName(internalName));
    }

    public Map<String, byte[]> getMap() {
        return map;
    }
}
