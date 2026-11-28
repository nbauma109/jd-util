import sys
import xml.etree.ElementTree as ET

NAMESPACE = "http://maven.apache.org/POM/4.0.0"
NAMESPACES = {"m": NAMESPACE}


def main(candidate_path, downstream_path):
    ET.register_namespace("", NAMESPACE)
    ET.register_namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance")

    candidate = ET.parse(candidate_path)
    version = candidate.findtext("m:version", namespaces=NAMESPACES)
    if not version:
        raise SystemExit("Cannot determine the jd-util candidate version")

    downstream = ET.parse(downstream_path)
    dependencies = [
        dependency
        for dependency in downstream.findall("m:dependencies/m:dependency", NAMESPACES)
        if dependency.findtext("m:groupId", namespaces=NAMESPACES) == "io.github.nbauma109"
        and dependency.findtext("m:artifactId", namespaces=NAMESPACES) == "jd-util"
    ]
    if len(dependencies) != 1:
        raise SystemExit("Expected exactly one direct jd-util dependency")

    dependency_version = dependencies[0].find("m:version", NAMESPACES)
    if dependency_version is None:
        raise SystemExit("Missing jd-util dependency version")

    dependency_version.text = version
    downstream.write(downstream_path, encoding="utf-8", xml_declaration=True)
    print(version)


if __name__ == "__main__":
    main(*sys.argv[1:])
