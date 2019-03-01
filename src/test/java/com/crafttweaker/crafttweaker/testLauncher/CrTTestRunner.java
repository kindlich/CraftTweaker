package com.crafttweaker.crafttweaker.testLauncher;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CrTTestRunner extends Runner {
    
    private static boolean initialized = false;
    
    private final Class<?> testClass;
    private final Map<Method, Description> tests;
    private final List<Method> beforeEach;
    private final List<Method> beforeAll;
    
    public CrTTestRunner(Class<?> testClass) {
        this.testClass = testClass;
        this.tests = new HashMap<>();
        this.beforeEach = new ArrayList<>();
        this.beforeAll = new ArrayList<>();
        
        for(Method method : testClass.getMethods()) {
            //TODO check for runtime parameters to only execute methods specified.
            if(method.isAnnotationPresent(Test.class)) {
                tests.put(method, Description.createTestDescription(testClass, method.getName(), method.getAnnotations()));
            } else if(method.isAnnotationPresent(BeforeClass.class)) {
                if(method.getParameterCount() != 0) {
                    throw new IllegalStateException("BeforeClass methods may not have any parameters!");
                }
                beforeAll.add(method);
            } else if(method.isAnnotationPresent(Before.class)) {
                if(method.getParameterCount() != 0) {
                    throw new IllegalStateException("BeforeClass methods may not have any parameters!");
                }
                beforeEach.add(method);
            }
        }
        
    }
    
    @Override
    public Description getDescription() {
        final Description all = Description.createSuiteDescription(testClass);
        
        for(Description value : tests.values()) {
            all.addChild(value);
        }
        
        return all;
    }
    
    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(getDescription());
        if(!initialized) {
            CrTMCLauncher.start();
            initialized = true;
        }
        final Object testInstance;
        try {
            testInstance = this.testClass.newInstance();
        } catch(InstantiationException | IllegalAccessException e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
            return;
        }
    
        try {
            for(Method method : this.beforeAll) {
                method.invoke(testInstance);
            }
        } catch(Exception e) {
            notifier.fireTestFailure(new Failure(getDescription(), e));
        }
        
        for(Map.Entry<Method, Description> methodDescriptionEntry : this.tests.entrySet()) {
            run(testInstance, notifier, methodDescriptionEntry.getKey(), methodDescriptionEntry.getValue());
        }
        
        notifier.fireTestFinished(getDescription());
    }
    
    
    private void run(Object testInstance, RunNotifier notifier, Method method, Description description) {
        notifier.fireTestStarted(description);
    
        try {
            for(Method beforeMethod : this.beforeEach) {
                beforeMethod.invoke(null);
            }
            method.invoke(testInstance);
        } catch(IllegalAccessException e) {
            notifier.fireTestFailure(new Failure(description, e));
        } catch(InvocationTargetException e) {
            notifier.fireTestFailure(new Failure(description, e.getTargetException()));
        }
    }
}
