package com.example.mytestmenu.entity_class;

import org.litepal.crud.LitePalSupport;

public class Avatar extends LitePalSupport {
    private int id;
    private String fileName;
    private String filePath;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
