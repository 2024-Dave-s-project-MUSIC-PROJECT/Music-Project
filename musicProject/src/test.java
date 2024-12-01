import processing.core.PApplet;//导入processing的core
import processing.core.PImage;//导入图形库
import ddf.minim.*;//导入minim库

public class test extends PApplet {
    PImage backgroundImage1;// 绘制背景;
    PImage backgroundImage2;
    Minim minim;
    AudioPlayer[] player = new AudioPlayer[100];//定义音乐的初始化变量
    int currentPage = 0;//定义页数PImage backgroundImage;//定义图片
    int barWidth=600;//进度条宽度

    public static void main(String[] args) {
        PApplet.main("test");
    }

    public void settings() {
        size(1500, 1250);
    }

    public void setup() {
        backgroundImage1 = loadImage("background1.png"); // 加载背景图片
        backgroundImage2 = loadImage("background2.png");
        minim = new Minim(this);
        player[1] = minim.loadFile("data/music1.mp3");
    }

    public void draw() {
        background(255);
        float progress;
        float mappedProgress;
        progress =  (float) player[1].position() / player[1].length();
        mappedProgress = map(progress, 0, 1, 0, barWidth);
        if (currentPage == 0) {
            drawPage1();
        } else if (currentPage == 1) {
            drawPage2();
            for (int i = 0; i < player[1].bufferSize() - 1; i++) {
                float x1 = map(i, 0, player[1].bufferSize(), 0, width);
                float x2 = map(i + 1, 0, player[1].bufferSize(), 0, width);
                line(x1, 50 + player[1].left.get(i) * 50, x2, 50 + player[1].left.get(i + 1) * 50);
                line(x1, 150 + player[1].right.get(i) * 50, x2, 150 + player[1].right.get(i + 1) * 50);

                fill(255, 255, 0);
                rect(100, (float) height / 2, mappedProgress, 20); // 绘制进度条
                // 显示当前音频时间
                fill(0);
                textSize(16);
                fill(0);
                text("Processing:"+(progress*100)+"%", 100, (float) height / 2 - 10);
            }
        }
    }

    void drawPage1() {

        image(backgroundImage1, 0, 0); // 绘制背景
//        fill(45, 26, 255);
//        textSize(70);
//        text("MusicPlayer", 400, 200);//展示文字
        fill(0, 255, 255);
        fill(0);
        textSize(32);
        text("page 1", width / 2, height / 2);
        drawButton("back to page 2", width / 2, height / 2 + 50);
    }

    void drawPage2() {
        background(0, 255, 255);
        image(backgroundImage2, 0, 0); // 绘制背景
        fill(0);
        textSize(32);
        text("page 2", width / 2, height / 2);
        textSize(36);
        text("press any key to pause",width/2,height/2-50);
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
            player[1].rewind();//倒带,从头播放
            player[1].play();//播放
        } else if (currentPage == 1 && mouseY > height / 2 + 25 && mouseY < height / 2 + 75) {
            currentPage = 0; // 切换到页面 1
            player[1].pause();
        }
    }

    public void keyPressed() {
        if (player[1].isPlaying()) {
            player[1].pause();      //键盘控制暂停
        }
        else if ( player[1].position() == player[1].length() )
        {
            player[1].rewind();
            player[1].play();
        }

        else
        {
            player[1].play();
        }
    }

}