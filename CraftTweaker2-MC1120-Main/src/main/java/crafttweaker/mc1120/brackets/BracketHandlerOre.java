package crafttweaker.mc1120.brackets;

import crafttweaker.*;
import crafttweaker.annotations.*;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.mc1120.oredict.MCOreDictEntry;
import crafttweaker.zenscript.GlobalRegistry;
import crafttweaker.zenscript.IBracketHandler;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.*;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.*;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Stan
 */
@BracketHandler(priority = 100)
@ZenClass("crafttweaker.brackets.bracketOre")
@ZenRegister
public class BracketHandlerOre implements IBracketHandler<IOreDictEntry> {

    @ZenMethod
    public static IOreDictEntry getOre(String name) {
        return new MCOreDictEntry(name);
    }
    
    public static List<IOreDictEntry> getOreList(String wildcardName) {
        List<IOreDictEntry> result = new ArrayList<>();
        Pattern wildcardPattern = Pattern.compile(wildcardName.replaceAll("\\*", ".+"));
        
        for(IOreDictEntry someOreDict : CraftTweakerAPI.oreDict.getEntries()) {
            String oreDictName = someOreDict.getName();
            if(wildcardPattern.matcher(oreDictName).matches()) {
                result.add(getOre(oreDictName));
            }
        }
        
        return result;
    }
    
    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if(tokens.size() > 2) {
            if(tokens.get(0).getValue().equals("ore") && tokens.get(1).getValue().equals(":")) {
                return find(environment, tokens, 2, tokens.size());
            }
        }
        
        return null;
    }
    
    private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
        StringBuilder valueBuilder = new StringBuilder();
        for(int i = startIndex; i < endIndex; i++) {
            Token token = tokens.get(i);
            valueBuilder.append(token.getValue());
        }
        IJavaMethod method = JavaMethod.get(GlobalRegistry.getTypes(), BracketHandlerOre.class, valueBuilder.toString().contains("*") ? "getOreList" : "getOre", String.class);
        return position -> new ExpressionCallStatic(position, environment, method, new ExpressionString(position, valueBuilder.toString()));
    }

	@Override
	public IOreDictEntry get(String name) {
		if(!name.toLowerCase().startsWith("ore:")) return null;
		return getOre(name.replaceFirst("ore:", ""));
	}
    
}
