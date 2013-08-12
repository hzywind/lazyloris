package com.lazyloris.modeler;

public class Env {

    private Env() {
    }

    private static Env instance;

    public static Env getInstance() {
        if (instance == null) {
            instance = new Env();
        }
        return instance;
    }

    private String modelFile;
    private String outputDirectory;

    public String getModelFile() {
        return modelFile;
    }

    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

}
