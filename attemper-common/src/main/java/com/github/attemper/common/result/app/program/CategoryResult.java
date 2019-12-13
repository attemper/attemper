package com.github.attemper.common.result.app.program;

import lombok.ToString;

@ToString
public class CategoryResult {

    protected String fileName;

    protected String parentFileName;

    protected String filePath;

    protected int dir;

    public String getFileName() {
        return fileName;
    }

    public CategoryResult setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getParentFileName() {
        return parentFileName;
    }

    public CategoryResult setParentFileName(String parentFileName) {
        this.parentFileName = parentFileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public CategoryResult setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public int getDir() {
        return dir;
    }

    public CategoryResult setDir(int dir) {
        this.dir = dir;
        return this;
    }
}
