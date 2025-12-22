/*
 * Copyright (c) 2025 GPLv3
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.parser.util;

import org.apache.commons.text.StringEscapeUtils;
import org.jd.core.v1.parser.ParseException;
import org.jd.core.v1.parser.Token;

import static org.jd.core.v1.parser.JdJavaSourceParserConstants.GT;

public class TextUtilities {

    private TextUtilities() {
    }

    /* We split >> / >>> into multiple > tokens when we are closing generic brackets. */
    public static void splitShiftTokenAsRightAngles(final Token shiftToken, final int rightAngleCount) {
      if (shiftToken == null || rightAngleCount <= 1) {
        return;
      }

      final Token originalNext = shiftToken.next;

      /* We mutate the consumed token into a single '>' */
      shiftToken.kind = GT;
      shiftToken.image = ">";

      Token cursor = shiftToken;

      /* We inject the remaining (rightAngleCount - 1) '>' tokens after it */
      for (int i = 1; i < rightAngleCount; i++) {
        final Token injected = Token.newToken(GT, ">");
        injected.beginLine = shiftToken.beginLine;
        injected.beginColumn = shiftToken.beginColumn;
        injected.endLine = shiftToken.endLine;
        injected.endColumn = shiftToken.endColumn;

        cursor.next = injected;
        cursor = injected;
      }

      cursor.next = originalNext;
    }

    public static int parseCharLiteral(final String tokenImage) throws ParseException {
        if (tokenImage == null) {
            throw new ParseException("Invalid char literal: null");
        }
        final int len = tokenImage.length();
        if (len < 3 || tokenImage.charAt(0) != '\'' || tokenImage.charAt(len - 1) != '\'') {
            throw new ParseException("Invalid char literal: " + tokenImage);
        }

        final String raw = tokenImage.substring(1, len - 1);
        final String unescaped = StringEscapeUtils.unescapeJava(raw);

        if (unescaped.length() != 1) {
            throw new ParseException("Invalid char literal: " + tokenImage);
        }

        return unescaped.charAt(0);
    }

    public static String parseStringLiteral(final String tokenImage) throws ParseException {
        if (tokenImage == null) {
            return "";
        }

        final int len = tokenImage.length();
        if (len < 2 || tokenImage.charAt(0) != '"' || tokenImage.charAt(len - 1) != '"') {
            throw new ParseException("Invalid string literal: " + tokenImage);
        }

        final String raw = tokenImage.substring(1, len - 1);
        return StringEscapeUtils.unescapeJava(raw);
    }

    public static String parseTextBlockLiteral(final String image) {
        String raw = image.substring(3, image.length() - 3);

        raw = raw.replace("\r\n", "\n").replace("\r", "\n");
        if (raw.startsWith("\n")) {
            raw = raw.substring(1);
        }

        final String[] lines = raw.split("\n", -1);

        int minIndent = Integer.MAX_VALUE;
        for (final String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            int indent = 0;
            while (indent < line.length()) {
                final char c = line.charAt(indent);
                if (c != ' ' && c != '\t') {
                    break;
                }
                indent++;
            }
            if (indent < minIndent) {
                minIndent = indent;
            }
        }

        if (minIndent == Integer.MAX_VALUE) {
            minIndent = 0;
        }

        final StringBuilder sb = new StringBuilder(raw.length());
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];

            int cut = 0;
            while (cut < minIndent && cut < line.length()) {
                final char c = line.charAt(cut);
                if (c != ' ' && c != '\t') {
                    break;
                }
                cut++;
            }

            sb.append(line.substring(cut));
            if (i < lines.length - 1) {
                sb.append('\n');
            }
        }

        return StringEscapeUtils.unescapeJava(sb.toString());
    }

    public static String stripNumericSeparators(final String literal) {
        if (literal.indexOf('_') < 0) {
            return literal;
        }
        return literal.replace("_", "");
    }

    public static String stripFloatingSuffix(final String literal) {
        if (literal.isEmpty()) {
            return literal;
        }
        final char last = literal.charAt(literal.length() - 1);
        if (last == 'f' || last == 'F' || last == 'd' || last == 'D') {
            return literal.substring(0, literal.length() - 1);
        }
        return literal;
    }

    public static int parseIntLiteral(final String image) {
        final String s = stripNumericSeparators(image);

        final int radix;
        final int start;

        if (s.startsWith("0x") || s.startsWith("0X")) {
            radix = 16;
            start = 2;
        } else if (s.startsWith("0b") || s.startsWith("0B")) {
            radix = 2;
            start = 2;
        } else if (s.length() > 1 && s.charAt(0) == '0') {
            radix = 8;
            start = 1;
        } else {
            radix = 10;
            start = 0;
        }

        return Integer.parseInt(s.substring(start), radix);
    }

    public static long parseLongLiteral(final String image) {
        String s = stripNumericSeparators(image);

        if (!s.isEmpty()) {
            final char last = s.charAt(s.length() - 1);
            if (last == 'l' || last == 'L') {
                s = s.substring(0, s.length() - 1);
            }
        }

        final int radix;
        final int start;

        if (s.startsWith("0x") || s.startsWith("0X")) {
            radix = 16;
            start = 2;
        } else if (s.startsWith("0b") || s.startsWith("0B")) {
            radix = 2;
            start = 2;
        } else if (s.length() > 1 && s.charAt(0) == '0') {
            radix = 8;
            start = 1;
        } else {
            radix = 10;
            start = 0;
        }

        return Long.parseLong(s.substring(start), radix);
    }

    public static float parseFloatLiteral(final String image) {
        final String normalized = stripFloatingSuffix(stripNumericSeparators(image));
        return Float.parseFloat(normalized);
    }

    public static double parseDoubleLiteral(final String image) {
        final String normalized = stripFloatingSuffix(stripNumericSeparators(image));
        return Double.parseDouble(normalized);
    }

}
