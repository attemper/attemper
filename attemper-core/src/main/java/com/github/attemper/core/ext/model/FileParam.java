package com.github.attemper.core.ext.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileParam {

    protected String localDirectory;

    protected String remotePath;

    protected String fileName;

    protected String suffix;

    protected String fileNameWithSuffix;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getLocalDirectory() != null) {
            sb.append("localDirectory(" + getLocalDirectory() + ") ");
        }
        if (getRemotePath() != null) {
            sb.append("remotePath(" + getRemotePath() + ") ");
        }
        if (getFileNameWithSuffix() != null) {
            sb.append("fileNameWithSuffix(" + getFileNameWithSuffix() + ") ");
        }
        return sb.toString();
    }
}
