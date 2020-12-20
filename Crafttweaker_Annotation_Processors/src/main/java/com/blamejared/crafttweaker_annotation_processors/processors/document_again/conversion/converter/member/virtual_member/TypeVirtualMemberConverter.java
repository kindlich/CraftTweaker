package com.blamejared.crafttweaker_annotation_processors.processors.document_again.conversion.converter.member.virtual_member;

import com.blamejared.crafttweaker_annotation_processors.processors.document_again.conversion.converter.member.MemberConverter;
import com.blamejared.crafttweaker_annotation_processors.processors.document_again.conversion.converter.type.TypeConverter;
import com.blamejared.crafttweaker_annotation_processors.processors.document_again.page.member.virtual_member.DocumentedTypeVirtualMembers;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;

public class TypeVirtualMemberConverter extends MemberConverter<DocumentedTypeVirtualMembers> {
    
    public TypeVirtualMemberConverter(TypeConverter typeConverter) {
        addElementConverter(ElementKind.METHOD, new VirtualMethodConverter(typeConverter));
    }
    
    @Override
    protected boolean isCandidate(Element enclosedElement) {
        return isVirtual(enclosedElement);
    }
    
    private boolean isVirtual(Element enclosedElement) {
        return !enclosedElement.getModifiers().contains(Modifier.STATIC);
    }
    
    @Override
    protected DocumentedTypeVirtualMembers createResultObject() {
        return new DocumentedTypeVirtualMembers();
    }
}
