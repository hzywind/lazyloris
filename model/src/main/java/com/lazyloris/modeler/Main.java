package com.lazyloris.modeler;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {

        String modelFile = getModelFile(args);
        String outputDirectory = getOutputDirectory(args);
        GenerateType type = getGenerateType(args);
        Env.getInstance().setModelFile(modelFile);
        Env.getInstance().setOutputDirectory(outputDirectory);
        
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "com/lazyloris/modeler/modelGeneratorContext.xml");
        Generator generator = (Generator) context.getBean("generator");
        generator.generate(type);
    }

    private static GenerateType getGenerateType(String[] args) {
        String type = args[2].toUpperCase();
        return GenerateType.valueOf(type);
    }

    private static String getOutputDirectory(String[] args) {
        return args[1];
    }

    private static String getModelFile(String[] args) {
        return args[0];
    }

}
