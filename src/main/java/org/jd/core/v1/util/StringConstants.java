/*******************************************************************************
 * Copyright (C) 2007-2019 Emmanuel Dupuy GPLv3
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.jd.core.v1.util;

public final class StringConstants
{
    private StringConstants() {
    }

    public static final String MIN_VALUE = "MIN_VALUE"; //$NON-NLS-1$
    public static final String MAX_VALUE = "MAX_VALUE"; //$NON-NLS-1$

    public static final String CLASS_CONSTRUCTOR = "<clinit>"; //$NON-NLS-1$
    public static final String INSTANCE_CONSTRUCTOR = "<init>"; //$NON-NLS-1$

    public static final String INTERNAL_JAVA_LANG_PACKAGE_NAME = "java/lang"; //$NON-NLS-1$
    public static final char   INTERNAL_PACKAGE_SEPARATOR = '/';
    public static final char   INTERNAL_INNER_SEPARATOR = '$';
    public static final char   INTERNAL_BEGIN_TEMPLATE = '<';

    public static final char   PACKAGE_SEPARATOR = '.';
    public static final char   INNER_SEPARATOR = '.';
    public static final String CLASS_FILE_SUFFIX = ".class"; //$NON-NLS-1$

    public static final String INTERNAL_CLASS_SIGNATURE = "Ljava/lang/Class;"; //$NON-NLS-1$
    public static final String INTERNAL_OBJECT_SIGNATURE = "Ljava/lang/Object;"; //$NON-NLS-1$
    public static final String INTERNAL_STRING_SIGNATURE = "Ljava/lang/String;"; //$NON-NLS-1$
    public static final String INTERNAL_DEPRECATED_SIGNATURE = "Ljava/lang/Deprecated;"; //$NON-NLS-1$
    public static final String INTERNAL_CLASSNOTFOUNDEXCEPTION_SIGNATURE =
        "Ljava/lang/ClassNotFoundException;"; //$NON-NLS-1$

    public static final String THIS_LOCAL_VARIABLE_NAME       = "this"; //$NON-NLS-1$
    public static final String OUTER_THIS_LOCAL_VARIABLE_NAME = "this$1"; //$NON-NLS-1$
    public static final String TMP_LOCAL_VARIABLE_NAME        = "tmp"; //$NON-NLS-1$

    public static final String START_OF_HEADING = "\u0001"; //$NON-NLS-1$

    public static final String ENUM_VALUES_ARRAY_NAME    = "$VALUES"; //$NON-NLS-1$
    public static final String ENUM_VALUES_ARRAY_NAME_ECLIPSE = "ENUM$VALUES"; //$NON-NLS-1$
    public static final String ENUM_VALUES_METHOD_NAME    = "values"; //$NON-NLS-1$
    public static final String ENUM_VALUEOF_METHOD_NAME    = "valueOf"; //$NON-NLS-1$
    public static final String TOSTRING_METHOD_NAME        = "toString"; //$NON-NLS-1$
    public static final String VALUEOF_METHOD_NAME        = "valueOf"; //$NON-NLS-1$
    public static final String APPEND_METHOD_NAME        = "append"; //$NON-NLS-1$
    public static final String FORNAME_METHOD_NAME        = "forName"; //$NON-NLS-1$
    public static final String ORDINAL_METHOD_NAME        = "ordinal"; //$NON-NLS-1$

    public static final String ANNOTATIONDEFAULT_ATTRIBUTE_NAME = "AnnotationDefault"; //$NON-NLS-1$
    public static final String CODE_ATTRIBUTE_NAME = "Code"; //$NON-NLS-1$
    public static final String CONSTANTVALUE_ATTRIBUTE_NAME = "ConstantValue"; //$NON-NLS-1$
    public static final String DEPRECATED_ATTRIBUTE_NAME = "Deprecated"; //$NON-NLS-1$
    public static final String ENCLOSINGMETHOD_ATTRIBUTE_NAME = "EnclosingMethod"; //$NON-NLS-1$
    public static final String EXCEPTIONS_ATTRIBUTE_NAME = "Exceptions"; //$NON-NLS-1$
    public static final String INNERCLASSES_ATTRIBUTE_NAME = "InnerClasses"; //$NON-NLS-1$
    public static final String LINENUMBERTABLE_ATTRIBUTE_NAME = "LineNumberTable"; //$NON-NLS-1$
    public static final String LOCALVARIABLETABLE_ATTRIBUTE_NAME = "LocalVariableTable"; //$NON-NLS-1$
    public static final String LOCALVARIABLETYPETABLE_ATTRIBUTE_NAME = "LocalVariableTypeTable"; //$NON-NLS-1$
    public static final String RUNTIMEINVISIBLEANNOTATIONS_ATTRIBUTE_NAME = "RuntimeInvisibleAnnotations"; //$NON-NLS-1$
    public static final String RUNTIMEVISIBLEANNOTATIONS_ATTRIBUTE_NAME = "RuntimeVisibleAnnotations"; //$NON-NLS-1$
    public static final String RUNTIMEINVISIBLEPARAMETERANNOTATIONS_ATTRIBUTE_NAME = "RuntimeInvisibleParameterAnnotations"; //$NON-NLS-1$
    public static final String RUNTIMEVISIBLEPARAMETERANNOTATIONS_ATTRIBUTE_NAME = "RuntimeVisibleParameterAnnotations"; //$NON-NLS-1$
    public static final String SIGNATURE_ATTRIBUTE_NAME = "Signature"; //$NON-NLS-1$
    public static final String SOURCEFILE_ATTRIBUTE_NAME = "SourceFile"; //$NON-NLS-1$
    public static final String SYNTHETIC_ATTRIBUTE_NAME = "Synthetic"; //$NON-NLS-1$
    public static final String BOOTSTRAP_METHODS_ATTRIBUTE_NAME = "BootstrapMethods"; //$NON-NLS-1$
    public static final String METHOD_PARAMETERS_ATTRIBUTE_NAME = "MethodParameters"; //$NON-NLS-1$

    public static final String CLASS_DOLLAR = "class$"; //$NON-NLS-1$
    public static final String ARRAY_DOLLAR = "array$"; //$NON-NLS-1$
    public static final String JD_METHOD_PREFIX = "jdMethod_"; //$NON-NLS-1$
    public static final String JD_FIELD_PREFIX = "jdField_"; //$NON-NLS-1$

    public static final String JAVA_LANG_STRING_BUFFER = "java/lang/StringBuffer"; //$NON-NLS-1$
    public static final String JAVA_LANG_STRING_BUILDER = "java/lang/StringBuilder"; //$NON-NLS-1$
    public static final String JAVA_LANG_STRING = "java/lang/String"; //$NON-NLS-1$
    public static final String JAVA_LANG_OBJECT = "java/lang/Object"; //$NON-NLS-1$
    public static final String JAVA_LANG_CLASS = "java/lang/Class"; //$NON-NLS-1$
    public static final String JAVA_LANG_MATH = "java/lang/Math"; //$NON-NLS-1$
    public static final String JAVA_LANG_NUMBER = "java/lang/Number"; //$NON-NLS-1$
    public static final String JAVA_LANG_THROWABLE = "java/lang/Throwable"; //$NON-NLS-1$
    public static final String JAVA_LANG_ASSERTION_ERROR = "java/lang/AssertionError"; //$NON-NLS-1$
    public static final String JAVA_LANG_ENUM = "java/lang/Enum"; //$NON-NLS-1$
    public static final String JAVA_LANG_EXCEPTION = "java/lang/Exception"; //$NON-NLS-1$
    public static final String JAVA_LANG_RUNTIME_EXCEPTION = "java/lang/RuntimeException"; //$NON-NLS-1$
    public static final String JAVA_LANG_COMPARABLE = "java/lang/Comparable"; //$NON-NLS-1$
    public static final String JAVA_LANG_CLONEABLE = "java/lang/Cloneable"; //$NON-NLS-1$
    public static final String JAVA_LANG_THREAD = "java/lang/Thread"; //$NON-NLS-1$
    public static final String JAVA_LANG_ITERABLE = "java/lang/Iterable"; //$NON-NLS-1$
    public static final String JAVA_LANG_SYSTEM = "java/lang/System"; //$NON-NLS-1$

    public static final String JAVA_LANG_VOID = "java/lang/Void"; //$NON-NLS-1$
    public static final String JAVA_LANG_BOOLEAN = "java/lang/Boolean"; //$NON-NLS-1$
    public static final String JAVA_LANG_BYTE = "java/lang/Byte"; //$NON-NLS-1$
    public static final String JAVA_LANG_SHORT = "java/lang/Short"; //$NON-NLS-1$
    public static final String JAVA_LANG_CHARACTER = "java/lang/Character"; //$NON-NLS-1$
    public static final String JAVA_LANG_INTEGER = "java/lang/Integer"; //$NON-NLS-1$
    public static final String JAVA_LANG_LONG = "java/lang/Long"; //$NON-NLS-1$
    public static final String JAVA_LANG_FLOAT = "java/lang/Float"; //$NON-NLS-1$
    public static final String JAVA_LANG_DOUBLE = "java/lang/Double"; //$NON-NLS-1$
    public static final String JAVA_UTIL_DATE = "java/util/Date"; //$NON-NLS-1$
}
