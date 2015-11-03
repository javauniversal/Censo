package censo.dito.co.censo.utils;

import censo.dito.co.censo.Entity.ConfigSplash;
import censo.dito.co.censo.cnst.Flags;

public class ValidationUtil {

    public static int hasPath(ConfigSplash cs) {
        if (cs.getPathSplash().isEmpty())
            return Flags.WITH_LOGO;
        else
            return Flags.WITH_PATH;
    }
}
