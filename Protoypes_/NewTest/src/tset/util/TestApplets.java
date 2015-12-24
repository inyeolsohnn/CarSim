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
    private boolean endInit;    //�жϴ����Ƿ��һ��paint���
    private int x0, y0, x1, y1, x2, y2;        //0Ϊ������ԭ��Ĵ������꣬1Ϊ��ʱ���꣬2Ϊʵʱ������������е�����
    private double unit;    //x�ᵥλ��С
    static int ballsize = 10;    //���ֱ��
    static Color backcolor = new Color(255, 255, 255);    //������ɫ
    static Color linecolor = new Color(0, 0, 255);    //���꼰�������ߵ���ɫ
    static int ballcolorrgb = 0x0;    //��ĳ�ʼ��ɫ��rgbֵ����ʮ�����Ʊ�ʾ
    public int interval = 8;    //�����е��ٶȣ������λ�ͼ���ʱ������ʾ

    BufferedImage imgcoordinate;    //���꼰������ͼƬ
    BufferedImage imgball;    //���ͼƬ
    Graphics2D gball;        //imgballͼƬ��Graphics

    public TestApplets() {    //���캯��
        super("�������������ƶ���С��");    //�趨���ڵı���
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        //�趨�رհ�ť������

        //�趨������ԭ��Ĵ�������
        this.x0 = 10;
        this.y0 = this.getHeight() / 2 + 10;

        //�����ڵĴ�С�仯�¼�,����������ԭ������꣬�ػ�����ͼƬ��
        addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
            	TestApplets obj = (TestApplets)e.getComponent();
                obj.x0 = 10;
                obj.y0 = obj.getHeight() / 2 + 10;
                createCoordinate();
                obj.repaint();
            }
        });

        //��ʼ��������Ҫ��ʼ����ֵ
        endInit = false;
        imgcoordinate = null;
        imgball = null;
    }

    //���������Ʊ���ͼƬ�����浽imgcoordinate��
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

    //����������С�򣬱��浽imgball��
    public void createBall() {
        imgball = new BufferedImage(ballsize, ballsize, BufferedImage.TYPE_3BYTE_BGR);
        gball = imgball.createGraphics();
        gball.setColor(backcolor);
        gball.fillRect(0, 0, ballsize, ballsize);
        gball.setColor(new Color(ballcolorrgb));
        gball.fillOval(0, 0, ballsize, ballsize);
    }

    //��д�����paint�¼���������ػ�ͼ����
    public void paint(Graphics g) {    
        if (imgcoordinate == null)
        {
            createCoordinate();        //��imgcoordinateΪnull�����ؽ�
        }
        if (imgball == null)
        {
            createBall();    //��imgballΪnull�����ؽ�
        }
        if (imgcoordinate != null)
        {
            g.drawImage(imgcoordinate, 0, 0, getWidth(), getHeight(), this);    //imgcoordinate��Ϊ�գ�����
        }
        if (imgball != null)
        {
            g.drawImage(imgball, x2 - 5, y2 - 5, 10, 10, this);        //imgball��Ϊ�գ�����
        }
        if (endInit == false)    //�ж��Ƿ�Ϊ��һ��paint����Ϊ�棬����endInitΪ�գ����������߳�
        {
            endInit = true;
            new Thread(this).start();
        }
    }

    //ʵ�ֽӿ�Runnable��run�¼��������̵߳���ػ�ͼ��������
    public void run() {
        while(endInit) {
            for(int i=0; i<=360; i++) {        //�һ��������
                x2 = (int)(x0 + i * unit);    //���㵱ǰ����
                y2 = y0 - (int)((y0 - 30) * Math.sin(i * Math.PI / 180.0));    
                try
                {
                    Thread.sleep(interval);        //ʵ���ٶȵĵ���
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                }
                gball.setColor(new Color(16777215 / 360 + 360 * i + ballcolorrgb));    //�ı������ɫ
                gball.fillOval(0, 0, ballsize, ballsize);
                repaint();    //����paint���������Ƶ�ǰʵʱͼ��
            }
        }
    }

    //���������������һ�����󣬲�����Ϊ�ɼ�
    public static void main(String[] args) {
    	TestApplets ball = new TestApplets();
        ball.setBounds(200, 200, 500, 300);
        ball.setVisible(true);
    }
}