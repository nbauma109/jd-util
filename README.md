[![Maven Central](https://img.shields.io/maven-central/v/io.github.nbauma109/jd-util.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.nbauma109/jd-util)
[![CodeQL](https://github.com/nbauma109/jd-util/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/nbauma109/jd-util/actions/workflows/codeql-analysis.yml)
[![Maven Release](https://github.com/nbauma109/jd-util/actions/workflows/maven.yml/badge.svg)](https://github.com/nbauma109/jd-util/actions/workflows/maven.yml)
[![Github Release](https://github.com/nbauma109/jd-util/actions/workflows/release.yml/badge.svg)](https://github.com/nbauma109/jd-util/actions/workflows/release.yml)
[![Coverage Status](https://codecov.io/gh/nbauma109/jd-util/branch/master/graph/badge.svg)](https://app.codecov.io/gh/nbauma109/jd-util)

# jd-util
Common utilities for Java bytecode analysis and type bindings used by jd-core projects, and java source parser/realigner/formatter

```java
import org.apache.bcel.classfile.Method;
import org.jd.core.v1.loader.ClassPathLoader;
import org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.model.javasyntax.type.ObjectType;
import org.jd.core.v1.parser.JavaParseResult;
import org.jd.core.v1.parser.JdJavaSourceParser;
import org.jd.core.v1.parser.ParseException;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.MethodTypes;
import org.jd.core.v1.service.converter.classfiletojavasyntax.util.TypeMaker.TypeTypes;
import org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import org.jd.core.v1.util.ParserRealigner;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class Sample {

    public static void main(String[] args) throws ParseException, IOException {
        /*
         * Inspect type
         */
        TypeMaker typeMaker = new TypeMaker();
        ObjectType ot = typeMaker.makeFromInternalTypeName("java/util/Map");
        System.out.println("Inner types : " + ot.getInnerTypeNames());
        TypeTypes typeTypes = typeMaker.makeTypeTypes("java/util/HashMap");
        System.out.println("Implemented interfaces : " + typeTypes.getInterfaces());
        System.out.println("Super type : " + typeTypes.getSuperType());
        MethodTypes methodTypes = typeMaker.makeMethodTypes("java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
        System.out.println("Parameter types : " + methodTypes.getParameterTypes());

        /*
         * Class deserializer 
         */
        ClassPathLoader classPathLoader = new ClassPathLoader();
        ClassFileDeserializer classFileDeserializer = new ClassFileDeserializer();
        ClassFile classFile = classFileDeserializer.loadClassFile(classPathLoader, "java/util/HashMap");
        Optional<Method> method = Stream.of(classFile.getMethods()).filter(m -> m.getName().equals("put")).findFirst();
        if (method.isPresent()) {
            typeTypes = typeMaker.parseClassFileSignature(classFile);
            System.out.println("Implemented interfaces : " + typeTypes.getInterfaces());
            System.out.println("Super type : " + typeTypes.getSuperType());
            methodTypes = typeMaker.parseMethodSignature(classFile, method.get());
            System.out.println("Parameter types : " + methodTypes.getParameterTypes());
        }
        
        /*
         * Parse and realign
         */
        JavaParseResult parsedSource = JdJavaSourceParser.parse("package demo; public class HelloWorld { public static void main(String[] args) { /* 5 */ System.out.println(\"Hello world!\"); } }");
        ParserRealigner parserRealigner = new ParserRealigner();
        String output = parserRealigner.format(parsedSource);
        System.out.println(output);
        
        /*
         * Just realign
         */
        System.out.println(parserRealigner.realign("package demo; public class HelloWorld { public static void main(String[] args) { /* 5 */ System.out.println(\"Hello world!\"); } }"));
    }

}
```
