package io.ib67.meta;

import io.ib67.Util;
import io.ib67.ast.decl.*;
import io.ib67.parser.meta.CatMetadata;
import lombok.AllArgsConstructor;

import java.lang.instrument.ClassDefinition;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClassMetaPathImpl implements MetaPath{
    private ClassLoader classLoader;
    @Override
    public CatMetadata findClass(String clazz) {
        CatMetadata cm = new CatMetadata();
        Class<?> claz = Util.runCatching(()->{
            return Class.forName(clazz,false,classLoader);
        }).getResult();
        if(claz==null){
            return null;
        }
        for (Field declaredField : claz.getDeclaredFields()) {
            if(!Modifier.isPublic(declaredField.getModifiers())) continue;
            VariableDef def = new VariableDef(declaredField.getType().getCanonicalName(),declaredField.getName());
            cm.getFields().put(declaredField.getName(),def);
        }
        for(Method declaredMethod: claz.getDeclaredMethods()){
            if(!Modifier.isPublic(declaredMethod.getModifiers()))continue;
            MethodSign sign = new MethodSign(declaredMethod.getName(), (ArrayList<String>) Arrays.stream(declaredMethod.getParameterTypes()).map(e->e.getCanonicalName()).collect(Collectors.toList()));
            cm.getMethods().add(sign);
        }
        ClassDef cdf = new ClassDef();
        cdf.setClassName(clazz);
        cdf.setSuperclass(claz.getSuperclass()==null?null:claz.getSuperclass().getCanonicalName());
        cdf.setInterfaces(Arrays.stream(claz.getInterfaces()).map(e->e.getCanonicalName()).collect(Collectors.toList()));
        cm.setClassDefinition(cdf);
        return cm;
    }
}
