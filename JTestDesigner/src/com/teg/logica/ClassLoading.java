package com.teg.logica;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.*;

/**
 *
 * @author maya
 */
public class ClassLoading {

    public static List getClassesNamesInPackage(String jarName, String packageName) {
        ArrayList classes = new ArrayList();
        packageName = packageName.replaceAll("\\.", "/");

        try {
            JarInputStream jarFile =
                    new JarInputStream(new FileInputStream(jarName));
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if ((jarEntry.getName().startsWith(packageName))
                        && (jarEntry.getName().endsWith(".class"))) {
                    classes.add(jarEntry.getName().replaceAll("/", "\\."));
                }
            }
        } catch (Exception e) {
        }
        return classes;
    }

    @SuppressWarnings("static-access")
    public ArrayList<Class> getClassDetails(String jar) throws MalformedURLException, ClassNotFoundException {
        ArrayList<Class> clasesJar = new ArrayList<Class>();
        File jarFile = new File(jar);

        URL[] urls = {jarFile.toURL()};
        URLClassLoader loader = new URLClassLoader(urls);
        Class clazz = null;

        List clases = this.getClassesNamesInPackage(jar, "");

        for (int i = 0; i < clases.size(); i++) {
            int pointIndex = clases.get(i).toString().lastIndexOf(".");
            String nombreClase = (String) clases.get(i).toString().subSequence(0, pointIndex);

            clazz = loader.loadClass(nombreClase);
            clasesJar.add(clazz);
//            this.printMembers(clazz.getDeclaredConstructors(), "Constuctors");
//            this.printMembers(clazz.getDeclaredFields(), "Fields");
//            this.printMembers(clazz.getDeclaredMethods(), "Methods");
        }
        return clasesJar;
    }

    @SuppressWarnings("static-access")
    public Class getClassDetail(String jar, String miClase) throws MalformedURLException, ClassNotFoundException {
        File jarFile = new File(jar);

        URL[] urls = {jarFile.toURL()};
        URLClassLoader loader = new URLClassLoader(urls);
        Class clazz = null;

        List clases = this.getClassesNamesInPackage(jar, "");

        for (int i = 0; i < clases.size(); i++) {
            int pointIndex = clases.get(i).toString().lastIndexOf(".");
            String nombreClase = (String) clases.get(i).toString().subSequence(0, pointIndex);

            if (nombreClase.equals(miClase)) {
                clazz = loader.loadClass(nombreClase);
            }
        }
        return clazz;
    }

    public void printMembers(Member[] mbrs, String s) {
        System.out.format("%s:%n", s);
        for (Member mbr : mbrs) {
            if (mbr instanceof Field) {
                System.out.format("  %s%n", ((Field) mbr).toGenericString());
            } else if (mbr instanceof Constructor) {
                System.out.format("  %s%n", ((Constructor) mbr).toGenericString());
            } else if (mbr instanceof Method) {
                System.out.format("  %s%n", ((Method) mbr).toGenericString());
            }
        }
        if (mbrs.length == 0) {
            System.out.format("  -- No %s --%n", s);
        }
        System.out.format("%n");
    }
}
