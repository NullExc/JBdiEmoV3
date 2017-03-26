package sk.tuke.fei.bdi.emotionalengine.BDIParser;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Peter on 14.3.2017.
 */
public class ClassReader {

    private static ClassReader classReader = new ClassReader();
    private Set<Class<?>> classes;

    public ClassReader() {

    }

    public static ClassReader getInstance() {
        return classReader;
    }

    public Set<Class<?>> getClasses(String pack) {
        if (classes == null) {
            classes = loadClasses(pack);
        }
        return classes;
    }

    private static Set<Class<?>> loadClasses(String pack) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(pack))));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        return classes;
    }
}
