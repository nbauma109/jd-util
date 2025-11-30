package jd.core.preferences;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreferencesTest {

    private Preferences preferences;

    @Before
    public void setUp() {
        preferences = new Preferences();
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertFalse(preferences.getShowDefaultConstructor());
        Assert.assertTrue(preferences.getRealignmentLineNumber());
        Assert.assertTrue(preferences.isShowPrefixThis());
        Assert.assertFalse(preferences.isUnicodeEscape());
        Assert.assertTrue(preferences.isShowLineNumbers());
        Assert.assertTrue(preferences.isWriteMetaData());
    }

    @Test
    public void testConstructorWithPreferencesMap() {
        Map<String, String> preferencesMap = new HashMap<String, String>();
        preferencesMap.put(Preferences.ESCAPE_UNICODE_CHARACTERS, "true"); //$NON-NLS-1$
        preferencesMap.put(Preferences.OMIT_THIS_PREFIX, "false"); //$NON-NLS-1$
        preferencesMap.put(Preferences.DISPLAY_DEFAULT_CONSTRUCTOR, "true"); //$NON-NLS-1$
        preferencesMap.put(Preferences.REALIGN_LINE_NUMBERS, "false"); //$NON-NLS-1$
        preferencesMap.put(Preferences.WRITE_LINE_NUMBERS, "false"); //$NON-NLS-1$
        preferencesMap.put(Preferences.WRITE_METADATA, "false"); //$NON-NLS-1$

        Preferences preferences = new Preferences(preferencesMap);

        Assert.assertTrue(preferences.isUnicodeEscape());
        Assert.assertTrue(preferences.isShowPrefixThis());
        Assert.assertTrue(preferences.getShowDefaultConstructor());
        Assert.assertFalse(preferences.getRealignmentLineNumber());
        Assert.assertFalse(preferences.isShowLineNumbers());
        Assert.assertFalse(preferences.isWriteMetaData());
    }

    @Test
    public void testConstructorWithEmptyPreferencesMap() {
        Preferences preferences = new Preferences(Collections.emptyMap());

        Assert.assertFalse(preferences.isUnicodeEscape());
        Assert.assertTrue(preferences.isShowPrefixThis());
        Assert.assertFalse(preferences.getShowDefaultConstructor());
        Assert.assertFalse(preferences.getRealignmentLineNumber());
        Assert.assertFalse(preferences.isShowLineNumbers());
        Assert.assertFalse(preferences.isWriteMetaData());
    }

    @Test
    public void testSetShowDefaultConstructor() {
        preferences.setShowDefaultConstructor(true);
        Assert.assertTrue(preferences.getShowDefaultConstructor());

        preferences.setShowDefaultConstructor(false);
        Assert.assertFalse(preferences.getShowDefaultConstructor());
    }

    @Test
    public void testSetRealignmentLineNumber() {
        preferences.setRealignmentLineNumber(true);
        Assert.assertTrue(preferences.getRealignmentLineNumber());

        preferences.setRealignmentLineNumber(false);
        Assert.assertFalse(preferences.getRealignmentLineNumber());
    }

    @Test
    public void testSetShowPrefixThis() {
        preferences.setShowPrefixThis(true);
        Assert.assertTrue(preferences.isShowPrefixThis());

        preferences.setShowPrefixThis(false);
        Assert.assertFalse(preferences.isShowPrefixThis());
    }

    @Test
    public void testSetUnicodeEscape() {
        preferences.setUnicodeEscape(true);
        Assert.assertTrue(preferences.isUnicodeEscape());

        preferences.setUnicodeEscape(false);
        Assert.assertFalse(preferences.isUnicodeEscape());
    }

    @Test
    public void testSetShowLineNumbers() {
        preferences.setShowLineNumbers(true);
        Assert.assertTrue(preferences.isShowLineNumbers());

        preferences.setShowLineNumbers(false);
        Assert.assertFalse(preferences.isShowLineNumbers());
    }

    @Test
    public void testSetWriteMetaData() {
        preferences.setWriteMetaData(true);
        Assert.assertTrue(preferences.isWriteMetaData());

        preferences.setWriteMetaData(false);
        Assert.assertFalse(preferences.isWriteMetaData());
    }
}
