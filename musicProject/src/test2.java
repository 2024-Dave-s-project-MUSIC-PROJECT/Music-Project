import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.*;

public class test2 extends PApplet {
    PImage backgroundImage1; // 绘制背景图片1
    PImage backgroundImage2; // 绘制背景图片2
    Minim minim; // Minim库实例，用于处理音频
    AudioPlayer[] player = new AudioPlayer[4]; // 定义音乐播放器数组，这里假设最多有3首音乐
    int currentPage = 0; // 当前显示的页面索引
    int currentMusicIndex = 1; // 当前播放的音乐索引
    int barWidth = 600; // 进度条宽度

    public static void main(String[] args) {
        PApplet.main("test2"); // 启动PApplet应用
    }

    public void settings() {
        size(1500, 1250); // 设置窗口大小
    }

    public void setup() {
        backgroundImage1 = loadImage("background1.png"); // 加载背景图片1
        backgroundImage2 = loadImage("background2.png"); // 加载背景图片2
        minim = new Minim(this); // 初始化Minim库
        player[1] = minim.loadFile("data/music1.mp3"); // 加载第一首音乐文件
        player[2] = minim.loadFile("data/music2.mp3"); // 加载第二首音乐文件
        player[3] = minim.loadFile("data/music3.mp3"); // 加载第三首音乐文件
    }

    public void draw() {
        float progress;
        float mappedProgress;

        // 检查当前播放的音乐是否存在并且正在播放
        if (player[currentMusicIndex] != null && player[currentMusicIndex].isPlaying()) {
            progress = (float) player[currentMusicIndex].position() / player[currentMusicIndex].length(); // 计算播放进度百分比
            mappedProgress = map(progress, 0, 1, 0, barWidth); // 将进度映射到进度条宽度
        } else {
            progress = 0;
            mappedProgress = 0;
        }

        // 根据当前页面绘制不同的内容
        if (currentPage == 0) {
            drawPage1(); // 绘制第一页
        } else if (currentPage == 1) {
            drawPage2(); // 绘制第二页

            // 如果当前音乐正在播放，则绘制音波图和进度条
            if (player[currentMusicIndex] != null && player[currentMusicIndex].isPlaying()) {
                for (int i = 0; i < player[currentMusicIndex].bufferSize() - 1; i++) {
                    float x1 = map(i, 0, player[currentMusicIndex].bufferSize(), 0, width); // 映射x坐标
                    float x2 = map(i + 1, 0, player[currentMusicIndex].bufferSize(), 0, width); // 映射下一个x坐标

                    // 绘制左声道音波图
                    stroke(255, 0, 0);
                    line(x1, 50 + player[currentMusicIndex].left.get(i) * 50, x2, 50 + player[currentMusicIndex].left.get(i + 1) * 50);

                    // 绘制右声道音波图
                    line(x1, 150 + player[currentMusicIndex].right.get(i) * 50, x2, 150 + player[currentMusicIndex].right.get(i + 1) * 50);

                    // 绘制进度条
                    fill(255, 255, 0);
                    noStroke();
                    rect(100, (float) height / 2, mappedProgress, 20); // 绘制进度条

                    // 显示剩余时间
                    fill(0);
                    textSize(16);
                    text("Remaining: " + formatTime(player[currentMusicIndex].length() - player[currentMusicIndex].position()), 100, (float) height / 2 - 10);
                }
            }
        }
    }

    void drawPage1() {
        image(backgroundImage1, 0, 0); // 绘制背景图片1
        fill(0);
        textSize(48);
        textAlign(CENTER, CENTER);
        drawButton("START", width / 2, height / 2 + 100); // 绘制按钮“START”
    }

    void drawPage2() {
        image(backgroundImage2, 0, 0); // 绘制背景图片2
        fill(0);
        textSize(48);
        textAlign(CENTER, CENTER);
        textSize(36);
        text("press any key to start/pause", width / 2, height / 2 - 100); // 提示信息
        drawButton("BACK", width / 2, height / 2 + 200); // 绘制按钮“BACK”
        drawButton("switch up", width / 2, height / 2 + 100); // 绘制按钮“switch up”
        drawButton("switch down", width / 2, height / 2 + 150); // 绘制按钮“switch down”
    }

    void drawButton(String label, float x, float y) {
        fill(192, 192, 192); // 设置按钮填充颜色
        stroke(128, 128, 128); // 设置按钮边框颜色
        strokeWeight(2); // 设置边框粗细
        rectMode(CENTER);
        rect(x, y, 200, 50, 10); // 绘制圆角矩形按钮

        fill(0);
        textSize(24);
        textAlign(CENTER, CENTER);
        text(label, x, y); // 在按钮上显示标签文本
    }

    public void mousePressed() {
        // 处理鼠标点击事件，根据位置切换页面或控制音乐
        if (currentPage == 0 && mouseY > height / 2 + 75 && mouseY < height / 2 + 125) {
            currentPage = 1; // 切换到页面2
        } else if (currentPage == 1 && mouseY > height / 2 + 175 && mouseY < height / 2 + 225) {
            currentPage = 0; // 切换到页面1
            stopCurrentMusic(); // 停止播放当前音乐
        } else if (currentPage == 1 && mouseY > height / 2 + 75 && mouseY < height / 2 + 125) {
            switchUp(); // 向上切换音乐
        } else if (currentPage == 1 && mouseY > height / 2 + 125 && mouseY < height / 2 + 175) {
            switchDown(); // 向下切换音乐
        }
    }

    public void keyPressed() {
        // 处理键盘按键事件，暂停或恢复播放音乐
        if (player[currentMusicIndex] != null) {
            if (player[currentMusicIndex].isPlaying()) {
                player[currentMusicIndex].pause(); // 暂停播放
            } else {
                player[currentMusicIndex].play(); // 恢复播放
            }
        }
    }

    void switchUp() {
        stopCurrentMusic(); // 停止当前播放的音乐
        currentMusicIndex++;
        if (currentMusicIndex >= player.length) {
            currentMusicIndex = 1;
        }
        playCurrentMusic(); // 开始播放新的音乐
    }

    void switchDown() {
        stopCurrentMusic(); // 停止当前播放的音乐
        currentMusicIndex--;
        if (currentMusicIndex < 1) {
            currentMusicIndex = player.length - 1;
        }
        playCurrentMusic(); // 开始播放新的音乐
    }

    void playCurrentMusic() {
        if (player[currentMusicIndex] != null) {
            player[currentMusicIndex].rewind(); // 重置音乐播放位置
            player[currentMusicIndex].play(); // 开始播放音乐
        }
    }

    void stopCurrentMusic() {
        if (player[currentMusicIndex] != null) {
            player[currentMusicIndex].pause(); // 暂停播放音乐
        }
    }

    String formatTime(int millis) {
        int seconds = millis / 1000; // 将毫秒转换为秒
        int minutes = seconds / 60; // 将秒转换为分钟
        seconds %= 60; // 获取剩余秒数
        return nf(minutes, 2) + ":" + nf(seconds, 2); // 格式化时间为MM:SS格式
    }

    void setGradient(float x, float y, float w, float h, int c1, int c2) {
        for (int i = (int)y; i <= (int)(y + h); i++) {
            float inter = map(i, y, y + h, 0, 1);
            int c = lerpColor(c1, c2, inter);
            stroke(c);
            line(x, i, x + w, i);
        }
    }
}



