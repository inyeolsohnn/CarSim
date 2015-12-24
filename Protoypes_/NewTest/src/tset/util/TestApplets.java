package tset.util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class TestApplets extends JFrame implements Runnable
{
    private boolean endInit;    //判断窗口是否第一次paint完毕
    private int x0, y0, x1, y1, x2, y2;        //0为坐标轴原点的窗口坐标，1为临时坐标，2为实时、球的坐标轴中的坐标
    private double unit;    //x轴单位大小
    static int ballsize = 10;    //球的直径
    static Color backcolor = new Color(255, 255, 255);    //背景颜色
    static Color linecolor = new Color(0, 0, 255);    //坐标及正弦曲线的颜色
    static int ballcolorrgb = 0x0;    //球的初始颜色的rgb值，以十六进制表示
    public int interval = 8;    //球运行的速度，用两次绘图间的时间间隔表示

    BufferedImage imgcoordinate;    //坐标及背景的图片
    BufferedImage imgball;    //球的图片
    Graphics2D gball;        //imgball图片的Graphics

    public TestApplets() {    //构造函数
        super("沿着正弦曲线移动的小球");    //设定窗口的标题
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //设定关闭按钮的作用

        //设定坐标轴原点的窗口坐标
        this.x0 = 10;
        this.y0 = this.getHeight() / 2 + 10;

        //处理窗口的大小变化事件,重置坐标轴原点的坐标，重画背景图片等
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
            	TestApplets obj = (TestApplets)e.getComponent();
                obj.x0 = 10;
                obj.y0 = obj.getHeight() / 2 + 10;
                createCoordinate();
                obj.repaint();
            }
        });

        //初始化其他需要初始化的值
        endInit = false;
        imgcoordinate = null;
        imgball = null;
    }

    //方法，绘制背景图片，保存到imgcoordinate中
    public void createCoordinate() {
        if (imgcoordinate != null)
        {
            imgcoordinate = null;
        }
        imgcoordinate = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = imgcoordinate.createGraphics();
        g.setColor(backcolor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(linecolor);
        g.drawLine(0, y0, getWidth(), y0);
        g.drawLine(x0, 0, x0, getHeight());
        x1 = x0;
        y1 = y0;
        unit = (getWidth() - 20.0) / 360;
        for(int i=0; i<=360; i++) {
            x2 = (int)(x0 + i * unit);
            y2 = y0 - (int)((y0 - 30) * Math.sin(i * Math.PI / 180.0));
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
        x2 = x0;
        y2 = y0;
    }

    //方法，绘制小球，保存到imgball中
    public void createBall() {
        imgball = new BufferedImage(ballsize, ballsize, BufferedImage.TYPE_3BYTE_BGR);
        gball = imgball.createGraphics();
        gball.setColor(backcolor);
        gball.fillRect(0, 0, ballsize, ballsize);
        gball.setColor(new Color(ballcolorrgb));
        gball.fillOval(0, 0, ballsize, ballsize);
    }

    //重写父类的paint事件，处理相关绘图事宜
    public void paint(Graphics g) {    
        if (imgcoordinate == null)
        {
            createCoordinate();        //若imgcoordinate为null，则重建
        }
        if (imgball == null)
        {
            createBall();    //若imgball为null，则重建
        }
        if (imgcoordinate != null)
        {
            g.drawImage(imgcoordinate, 0, 0, getWidth(), getHeight(), this);    //imgcoordinate不为空，绘制
        }
        if (imgball != null)
        {
            g.drawImage(imgball, x2 - 5, y2 - 5, 10, 10, this);        //imgball不为空，绘制
        }
        if (endInit == false)    //判断是否为第一次paint，若为真，则置endInit为空，并启动新线程
        {
            endInit = true;
            new Thread(this).start();
        }
    }

    //实现接口Runnable的run事件，处理线程的相关绘图动作安排
    public void run() {
        while(endInit) {
            for(int i=0; i<=360; i++) {        //活动一个正弦线
                x2 = (int)(x0 + i * unit);    //计算当前坐标
                y2 = y0 - (int)((y0 - 30) * Math.sin(i * Math.PI / 180.0));    
                try
                {
                    Thread.sleep(interval);        //实现速度的调节
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
                gball.setColor(new Color(16777215 / 360 + 360 * i + ballcolorrgb));    //改变球的颜色
                gball.fillOval(0, 0, ballsize, ballsize);
                repaint();    //调用paint方法，绘制当前实时图像
            }
        }
    }

    //类的主函数，定义一个对象，并设置为可见
    public static void main(String[] args) {
    	TestApplets ball = new TestApplets();
        ball.setBounds(200, 200, 500, 300);
        ball.setVisible(true);
    }
}