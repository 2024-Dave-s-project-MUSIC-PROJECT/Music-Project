import processing.core.PApplet;
import processing.core.PImage;


public class Main extends PApplet {
    PImage backgroundImage;
    private boolean buttonHovered = false;
    private boolean buttonPressed = false;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings() {
        size(1000, 1100); // 设置窗口大小
    }

    public void setup() {
        backgroundImage = loadImage("background.jpg"); // 加载背景图片
    }

    public void draw() {
        background(255); // 设置背景色
        image(backgroundImage, 0, 0); // 绘制背景
        fill(0,255,255);
        textSize(150);
        text("MusicPlayer",100,200);//展示文字
        // 绘制按钮
        if (buttonHovered) {
            fill(200);
        } else {
            fill(150);
        }
        rect(500, 500, 130, 70);

        // 按钮文本
        fill(148,0,211);
        textSize(50);
        text("CLICK", 500, 550);
    }

    public void mouseMoved() {
        // 检测鼠标是否在按钮上
        buttonHovered = mouseX > 500 && mouseX < 630  && mouseY > 500 && mouseY < 570;
    }


    public void mousePressed() {
        // 检测按钮被按下
        if (buttonHovered) {
            buttonPressed = true;
            textSize(50);
            text("WELCOME", 500, 700);

        }
    }

    public void mouseReleased() {
        buttonPressed = false;
    }

}