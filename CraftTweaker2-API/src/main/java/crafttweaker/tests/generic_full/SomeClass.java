package crafttweaker.tests.generic_full;

import crafttweaker.*;
import crafttweaker.annotations.*;
import crafttweaker.api.item.*;
import crafttweaker.tests.generic_full.type.*;
import crafttweaker.zenscript.*;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.type.*;

import java.util.*;

@ZenClass("crafttweaker.api.some.Class")
@ZenRegister
public class SomeClass {
    
    @OnRegister
    public static void onRegister() {
        CraftTweakerAPI.logInfo("Called onRegister");
        
    }
    
    @ZenMethod
    public static MySequence<IItemStack> getIItemStackSequence() {
        return new MySequence<>(new ArrayList<>());
    }
    
    @ZenMethod
    public static MySequence<String> getStringSequence() {
        return new MySequence<>(Arrays.asList(
                "A", "B", "C", "D", "E"
        ));
    }
}
