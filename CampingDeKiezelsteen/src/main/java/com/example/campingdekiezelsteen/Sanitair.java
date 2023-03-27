package com.example.campingdekiezelsteen;

public class Sanitair extends Building {
    private String style = "sanitair";

    public Sanitair(){
        setName("Sanitair");
    }

    @Override
    public void cleanToilets() {
        super.cleanToilets();
    }

    @Override
    public void cleanSinks() {
        super.cleanSinks();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
