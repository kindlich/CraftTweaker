package crafttweaker.api.TEST;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("crafttweaker.tests.enumUser")
@ZenRegister
public class EnumUser {

	@ZenMethod
	public static void getNumber(EnumTest eT) {
		CraftTweakerAPI.logInfo(String.valueOf(eT.getId()));
	}

}
