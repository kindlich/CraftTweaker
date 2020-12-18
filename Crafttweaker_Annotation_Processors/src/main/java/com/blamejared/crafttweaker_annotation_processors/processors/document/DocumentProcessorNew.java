package com.blamejared.crafttweaker_annotation_processors.processors.document;

import com.blamejared.crafttweaker_annotation_processors.processors.AnnotationMirrorUtil;
import com.blamejared.crafttweaker_annotation_processors.processors.document.yaml.YAMLFile;
import com.blamejared.crafttweaker_annotation_processors.processors.document.yaml.YAMLFolder;
import com.blamejared.crafttweaker_annotation_processors.processors.document.yaml.YAMLTypeAdapter;
import com.blamejared.crafttweaker_annotation_processors.processors.util.files.DeletingPathVisitor;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SupportedAnnotationTypes({"com.blamejared.crafttweaker_annotations.annotations.Document", "net.minecraftforge.fml.common.Mod"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DocumentProcessorNew extends AbstractProcessor {
    
    private static final File docsOut = new File("docsOut");
    private static final Set<CrafttweakerDocumentationPage> pages = new TreeSet<>(Comparator.comparing(CrafttweakerDocumentationPage::getDocumentTitle));
    public static Map<String, String> modIdByPackage = new HashMap<>();
    public static Trees tree;
    private final Collection<Element> allElements = new HashSet<>();
    
    public static String getModIdForPackage(Element element, ProcessingEnvironment environment) {
        final PackageElement elementPackage = environment.getElementUtils().getPackageOf(element);
        final String packageName = elementPackage.getQualifiedName().toString();
        
        for(String knownPackName : modIdByPackage.keySet()) {
            if(packageName.startsWith(knownPackName)) {
                return modIdByPackage.get(knownPackName);
            }
        }
        return null;
    }
    
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        tree = Trees.instance(processingEnv);
    }
    
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        fillModIdInfo(roundEnv);
        allElements.addAll(roundEnv.getElementsAnnotatedWith(Document.class));
        if(!roundEnv.processingOver()) {
            return false;
        }
        
        //Update each element to its latest state, then handle them all
        allElements.stream().map(Object::toString) //Get names
                .map(processingEnv.getElementUtils()::getTypeElement).forEach(this::handleElement);
        
        clearOutputDir();
        writeToFiles();
        allElements.clear();
        return false;
    }
    
    private void handleElement(TypeElement typeElement) {
        final Document document = typeElement.getAnnotation(Document.class);
        if(document == null) {
            this.processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "Internal error! Document annotation null", typeElement);
            return;
        }
        
        if(!typeElement.getKind().isClass() && !typeElement.getKind().isInterface()) {
            this.processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "How is this annotated", typeElement);
            return;
        }
        
        
        final CrafttweakerDocumentationPage documentationPage = CrafttweakerDocumentationPage.convertType(typeElement, this.processingEnv);
        if(documentationPage != null) {
            pages.add(documentationPage);
        }
    }
    
    private void clearOutputDir() {
        if(!docsOut.exists()) {
            return;
        }
        
        if(!docsOut.isDirectory()) {
            throw new IllegalStateException("File " + docsOut + " exists and is not a directory!");
        }
        
        try {
            final Path outputPath = docsOut.getAbsoluteFile().toPath();
            Files.walkFileTree(outputPath, new DeletingPathVisitor());
        } catch(IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }
    
    private void fillModIdInfo(RoundEnvironment roundEnv) {
        final Elements elementUtils = processingEnv.getElementUtils();
        final TypeElement typeElement = elementUtils.getTypeElement("net.minecraftforge.fml.common.Mod");
        
        for(Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
            final AnnotationMirror modAnnotation = AnnotationMirrorUtil.getMirror(element, typeElement);
            final String modId = AnnotationMirrorUtil.getAnnotationValue(modAnnotation, "value");
            
            final PackageElement modPackage = elementUtils.getPackageOf(element);
            modIdByPackage.put(modPackage.getQualifiedName().toString(), modId);
        }
    }
    
    private void writeToFiles() {
        final File docsFolder = new File(docsOut, "docs");
        
        //Create folder
        if(!docsFolder.exists() && !docsFolder.mkdirs()) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.ERROR, "Could not create folder " + docsFolder.getAbsolutePath());
            return;
        }
        
        //Create files
        try {
            for(CrafttweakerDocumentationPage page : pages) {
                page.write(docsFolder, processingEnv);
            }
            writeYAML();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeYAML() throws IOException {
        
        final List<CrafttweakerDocumentationPage> values = new ArrayList<>(CrafttweakerDocumentationPage.knownTypes
                .values());
        values.sort(Comparator.comparing(CrafttweakerDocumentationPage::getDocPath));
        final Map<String, YAMLFolder> files = new HashMap<>();
        for(CrafttweakerDocumentationPage value : values) {
            
            String path = value.getDocPath().contains("/") ? value.getDocPath()
                    .substring(0, value.getDocPath().lastIndexOf("/")) : value.getDocPath();
            String[] topFolders = path.split("/");
            for(int i = 0; i < topFolders.length; i++) {
                topFolders[i] = topFolders[i].toUpperCase().charAt(0) + topFolders[i].substring(1);
            }
            YAMLFolder folder = files.computeIfAbsent(topFolders[0], YAMLFolder::new);
            if(topFolders.length > 1) {
                for(int i = 1; i < topFolders.length; i++) {
                    YAMLFolder file = folder.getOrCreate(topFolders[i]);
                    folder.addFile(file);
                    folder = file;
                }
            }
            folder.addFile(new YAMLFile(value.getDocumentTitle(), value.getDocPath()));
        }
        
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(YAMLFolder.class, new YAMLTypeAdapter())
                .create();
        
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File(docsOut, "docs.json")))) {
            Map<String, Map<String, YAMLFolder>> nav = new HashMap<>();
            nav.put("nav", files);
            writer.write(gson.toJson(nav));
        }
    }
    
    
}
