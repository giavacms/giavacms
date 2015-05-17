package org.giavacms.base.util;

import org.jboss.logging.Logger;

/**
 * <system-properties> <property name="ucs-postel.WORKING_DIRECTORY" value="ciao ciao"/> <property name="foo"
 * value="bar"/> <property name="ucs-postel.EXEC_DIRECTORY" value="/exec"/> </system-properties>
 *
 * @author pisi79
 */
public class SystemPropertiesUtils {

    static Logger logger = Logger.getLogger(SystemPropertiesUtils.class);

    public static String getProperty(String prefix, String name) {
        return System.getProperty(prefix + "." + name);
    }

}
