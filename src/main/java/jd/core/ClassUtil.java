/*******************************************************************************
 * Copyright (C) 2022 GPLv3
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
package jd.core;

public final class ClassUtil {

    private ClassUtil() {
    }

    public static String getInternalName(String s) {
        if (!s.endsWith(".class")) {
            throw new IllegalArgumentException("Not a .class file: " + s);
        }
        return s.substring(0, s.length() - 6); // 6 = ".class".length()
    }
}
