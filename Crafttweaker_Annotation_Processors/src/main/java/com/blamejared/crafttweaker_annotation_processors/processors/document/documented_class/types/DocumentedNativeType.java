package com.blamejared.crafttweaker_annotation_processors.processors.document.documented_class.types;

public class DocumentedNativeType extends DocumentedType {
    public final String ZSName;

    public DocumentedNativeType(String zsName) {
        ZSName = zsName;
    }

    @Override
    public String getZSName() {
        return ZSName;
    }

    @Override
    public String getClickableMarkdown() {
        //We don't interlink to native pages, do we?
        return ZSName;
    }
}