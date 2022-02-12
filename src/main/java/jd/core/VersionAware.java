package jd.core;

import org.jd.core.v1.util.StringConstants;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public interface VersionAware {

    default String getVersion() throws IOException {
        String className = getClass().getSimpleName() + StringConstants.CLASS_FILE_SUFFIX;
        String classPath = getClass().getResource(className).toString();
        if (!classPath.startsWith("jar")) {
            return "SNAPSHOT";
        }
        URL url = new URL(classPath);
        JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
        Manifest manifest = jarConnection.getManifest();
        Attributes attributes = manifest.getMainAttributes();
        return attributes.getValue("jdCore.version");
    }
}
