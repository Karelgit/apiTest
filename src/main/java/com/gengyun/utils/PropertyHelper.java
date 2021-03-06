package com.gengyun.utils;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author lihzh-home
 */
public class PropertyHelper {

    private ResourceBundle propBundle;

    public PropertyHelper(String bundle) {
        propBundle = PropertyResourceBundle.getBundle(bundle);
    }

    public String getValue(String key) {
        return this.propBundle.getString(key);
    }

}
