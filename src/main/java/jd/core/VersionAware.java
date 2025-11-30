package jd.core;

import org.jd.core.v1.util.StringConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public interface VersionAware {

    default String getVersion() throws IOException {
        return getMainAttribute("JD-Core-Version"); //$NON-NLS-1$
    }

    default String getMainAttribute(String attributeName) throws IOException {
        return getMainAttribute(getClass(), attributeName);
    }

    default String getMainAttribute(Class<?> clazz, String attributeName) throws IOException {
        String className = clazz.getSimpleName() + StringConstants.CLASS_FILE_SUFFIX;
        String classPath = clazz.getResource(className).toString();
        if (!classPath.startsWith("jar")) { //$NON-NLS-1$
            return findFirstMainAttribute(attributeName);
        }
        URL url = new URL(classPath);
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        Manifest manifest = jarConnection.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        return attributes.getValue(attributeName);
    }

    default String findFirstMainAttribute(String attributeName) throws IOException {
        Enumeration<URL> manifestURLs = getClass().getClassLoader().getResources("META-INF/MANIFEST.MF"); //$NON-NLS-1$
        while (manifestURLs.hasMoreElements()) {
            URL manifestUrl = manifestURLs.nextElement();
            try (InputStream inputStream = manifestUrl.openStream()) {
                Manifest manifest = new Manifest(inputStream);
                Attributes attributes = manifest.getMainAttributes();
                String attributeValue = attributes.getValue(attributeName);
                if (attributeValue != null) {
                    return attributeValue;
                }
            }
        }
        return "SNAPSHOT"; //$NON-NLS-1$
    }
}
