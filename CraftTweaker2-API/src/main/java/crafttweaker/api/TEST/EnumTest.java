package crafttweaker.api.TEST;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.*;


@ZenClass("crafttweaker.tests.enumtest")
@ZenRegister
public class EnumTest {
	private final int id;
	
	public static final EnumTest one = new EnumTest(1);
	public static final EnumTest two = new EnumTest(2);
	public static final EnumTest three = new EnumTest(3);
	public static final EnumTest four = new EnumTest(4);
	
	
	public EnumTest(int i) {
		this.id = i;
	}
	
	@ZenGetter("id")
	public int getId(){
		return id;
	}
	@ZenMethod
	public static void otherPrint(EnumTest t) {
		CraftTweakerAPI.logInfo(String.valueOf(t.getId()));
	}
	
	@ZenOperator(OperatorType.COMPARE)
	@ZenMethod
	public boolean equals(EnumTest other) {
		return id == other.id;
	}
	
//	@ZenMethod
//	public static EnumTest one() {
//		System.out.println("FUNCTION ONE() CALLED");
//		return new EnumTest(1);
//	}
//	
//	@ZenMethod
//	public static EnumTest getOne() {
//		System.out.println("FUNCTION GETONE() CALLED");
//		return one();
//	}

}
