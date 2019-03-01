package com.crafttweaker.crafttweaker;

import com.crafttweaker.crafttweaker.vanilla.crafting.internal.CrTRecipeShapedTest;
import com.crafttweaker.crafttweaker.vanilla.crafting.internal.CrTRecipeShapelessTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CrTRecipeShapedTest.class, CrTRecipeShapelessTest.class})
public class AllTests {}
