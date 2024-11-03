import processing.core.PApplet;
import processing.core.PImage;

public class test extends PApplet {

    int currentPage = 0;
    PImage backgroundImage;
    public static void main(String[] args) {
        PApplet.main("test");
    }

    public void settings() {
        size(800, 1052);
    }

    public void setup() {
        backgroundImage = loadImage("background.jpg"); // 加载背景图片
    }

    public void draw() {
        background(255);
        if (currentPage == 0) {
            drawPage1();
        } else if (currentPage == 1) {
            drawPage2();
        }
    }

    void drawPage1() {

        image(backgroundImage, 0, 0); // 绘制背景
        fill(45,26,255);
        textSize(70);
        text("MusicPlayer",400,200);//展示文字
        fill(0,255,255);
        fill(0);
        textSize(32);
        text("page 1", width / 2, height / 2);
        drawButton("back to page 2", width / 2, height / 2 + 50);
    }

    void drawPage2() {
        background(0,255,255);
        fill(0);
        textSize(32);
        text("page 2", width / 2, height / 2);
        drawButton("back to page1", width / 2, height / 2 + 50);
    }

    void drawButton(String label, float x, float y) {
        fill(200);
        rectMode(CENTER);
        rect(x, y, 200, 50);
        fill(0);
        textSize(16);
        textAlign(CENTER, CENTER);
        text(label, x, y);
    }

    public void mousePressed() {
        if (currentPage == 0 && mouseY > height / 2 + 25 && mouseY < height / 2 + 75) {
            currentPage = 1; // 切换到页面 2
        } else if (currentPage == 1 && mouseY > height / 2 + 25 && mouseY < height / 2 + 75) {
            currentPage = 0; // 切换到页面 1
        }
    }
}