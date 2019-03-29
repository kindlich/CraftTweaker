package com.crafttweaker.crafttweaker.api;

import com.crafttweaker.crafttweaker.api.action.AbstractAction;
import com.crafttweaker.crafttweaker.api.logger.MultiLogger;

import javax.annotation.Nonnull;

public class CraftTweakeAPI {
    
    public static final MultiLogger logger = new MultiLogger();
    
    public static void apply(@Nonnull AbstractAction abstractAction) {
        if(!abstractAction.isValid()) {
            final String s = abstractAction.describeInvalid();
            if(s != null)
                logger.logError(abstractAction.getLocation() + ": " + s);
            return;
        }
        try {
            logger.logInfo(abstractAction.describeValid());
            abstractAction.apply();
            logger.logInfo(abstractAction.describePost());
        } catch(Exception e) {
            logger.logError(abstractAction.getLocation() + ": " + abstractAction.describeFail(e));
        }
    }
}
