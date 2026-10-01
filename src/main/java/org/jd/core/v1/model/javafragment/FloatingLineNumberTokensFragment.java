/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */
package org.jd.core.v1.model.javafragment;

import org.jd.core.v1.model.token.Token;

import java.util.List;

/**
 * Tokens of a statement which has a known line number, but which is out of order with regard to the statements
 * around it (e.g. the update of a 'for' loop copied before a 'continue', or the copy of a 'finally' block inlined
 * by the compiler). The statement can not be aligned on its line number: it floats between its neighbours, like a
 * {@link TokensFragment}. Its line number is nevertheless kept, to be reported to the printer.
 */
public class FloatingLineNumberTokensFragment extends TokensFragment {

    public FloatingLineNumberTokensFragment(List<Token> tokens) {
        super(0, tokens);
    }
}
