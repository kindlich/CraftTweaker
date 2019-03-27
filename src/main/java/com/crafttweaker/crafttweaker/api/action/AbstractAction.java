package com.crafttweaker.crafttweaker.api.action;

import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public abstract class AbstractAction {
    
    /**
     * ZC file extensions to look for in the stack trace
     */
    private static final String[] extensions = new String[]{"zs", "zc"};
    
    /**
     * The file and line of the ZC script responsible for this action.
     * <code>?:?</code> if not found.
     */
    private final String location;
    
    protected AbstractAction() {
        this.location = Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(s -> FilenameUtils.isExtension(s.getFileName(), extensions))
                .findFirst()
                .map(s -> s.getFileName() + ":" + s.getLineNumber())
                .orElse("?:?");
    }
    
    @Nonnull
    @Contract(pure = true)
    public String getLocation() {
        return location;
    }
    
    /**
     * Verify that the action is valid.
     * Only valid actions will have their {@link #apply()} method invoked
     * If not {@link #describeInvalid()} will be logged.
     */
    public abstract boolean isValid();
    
    /**
     * Use this to explain why an action is not valid.
     * Return <code>null</code> if nothing should be logged.
     */
    @Nullable
    public abstract String describeInvalid();
    
    /**
     * Describes the action to be executed.
     * Invoked if {@link #isValid()} returned <code>true</code>
     * Invoked before {@link #apply()} is invoked.
     */
    @Nonnull
    public abstract String describeValid();
    
    /**
     * Apply the action.
     * Will be executed after is has been described using {@link #describeValid()}.
     */
    public abstract void apply();
    
    /**
     * Notify that the action has been successfully applied.
     * Return <code>null</code> if nothing should be logged
     */
    @Nullable
    public String describePost() {
        return null;
    }
    
    /**
     * Called if {@link #apply()} threw an exception.
     * Usually this should never be invoked.
     * @param e The caught exception
     */
    @Nonnull
    public String describeFail(@Nonnull Exception e) {
        return getLocation() + ": " + e.getLocalizedMessage();
    }
}
