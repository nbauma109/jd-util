/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.loader;

import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.util.StringConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathLoader implements Loader {

    private static String getPathForInternalName(String internalName) {
        StringBuilder path = new StringBuilder();
        path.append('/');
        path.append(internalName);
        if (!internalName.endsWith(StringConstants.CLASS_FILE_SUFFIX)) {
            path.append(StringConstants.CLASS_FILE_SUFFIX);
        }
        return path.toString();
    }

    @Override
    public byte[] load(String internalName) throws IOException {
        InputStream is = this.getClass().getResourceAsStream(getPathForInternalName(internalName));

        if (is == null) {
            return null;
        }
        try (InputStream in=is; ByteArrayOutputStream out=new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024 * 2];
            int read = in.read(buffer);

            while (read > 0) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }

            return out.toByteArray();
        }
    }

    @Override
    public boolean canLoad(String internalName) {
        return this.getClass().getResource(getPathForInternalName(internalName)) != null;
    }
}
