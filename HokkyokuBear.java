
import java.awt.*;
import java.awt.geom.AffineTransform;

class HokkyokuBear extends Canvas {
    double x, y, dx, dy, w, h, ds, dsr, xc, yc, t;
    double xAngle, yAngle , xcAngle, ycAngle;
    int k;
    int s=32, cond1=0, cw=0, cwMax=5;
    // cond1: キャラの右足が前 (1) か、揃っている（０）か、左足が前 (-1) か
    int imgX, imgY;
    boolean jumpFlag = false;
    Image sirokumaImage = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/bear-polar.png");
    Image sirokumaImage2 = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/bear-polarのコピー.png");

    HokkyokuBear(int apWidth, int apHeight){
        x  = (int)(apWidth/2-20);    // 画面の真中
        y  = (int)(apHeight/2-20); // 画面の下の方
        dx = 5;
        dy = 5;
        ds = 12;
        dsr = ds/Math.sqrt(2);
        k = 0;
        w  = 15;
        h  = 30;
        xAngle = x;
        yAngle = y - 3;
        
    }

    void picDrawRight(Graphics2D g){        //右側を向いている時の画像処理
        int imgX, imgY;
        
        if(cond1 == 1 ){imgY = s*3;}
        else if(cond1 == 0){imgY = 0;}
        else{imgY = s*4;}
        imgX = 0;
        if(jumpFlag==false){g.drawImage(sirokumaImage, (int)x-45, (int)y-35, (int)x+50, (int)y+60, imgX, imgY, imgX+s, imgY+s,null);}
        
        
    }

    void picDrawLeft(Graphics g){       //左の画像処理
        int imgX, imgY;
        
        if(cond1 == 1 ){imgY = s*3;}
        else if(cond1 == 0){imgY = 0;}
        else{imgY = s*4;}
        imgX = 0;
        if(jumpFlag==false){g.drawImage(sirokumaImage2, (int)x-45, (int)y-35, (int)x+50, (int)y+60, imgX, imgY, imgX+s, imgY+s,null);}
        
    }

    void move(Graphics buf, int apWidth, int apHeight, int t){      //描画の関数
        buf.setColor(Color.blue);
        Graphics2D g2 = (Graphics2D)buf;
		angle(t,g2);
        //g2.fillRect((int)(x - w), (int)(y - h), 2 * (int)w, 2 * (int)h);
        //g2.drawLine(x-3, y-3, x+3, y+3);
        if(t==1 || t==2 || t==3 || t==4){
            picDrawRight(g2);
        }
        else{
            picDrawLeft(g2);
        }
        //g2.drawLine((int)x-3, (int)y-3, (int)x+3, (int)y+3);
    }

    

    void jump(Graphics buf, int t, int k){
        Graphics2D g2 = (Graphics2D)buf;
        angle(t,g2);
        //g2.fillRect((int)(x - w), (int)(y - h), 2 * (int)w, 2 * (int)h);
        if(k>0 && k<=20){           //カウンタの数字ごとに画像の大きさを変更してジャンプを演出する
            if(t==1 || t==2 || t==3 || t==4){
                //picDrawRight(g2);
                g2.drawImage(sirokumaImage, (int)x-15, (int)y-15, (int)x+80, (int)y+80, imgX, imgY, imgX+s, imgY+s,null);
            }
            else{
                //picDrawLeft(g2);
                g2.drawImage(sirokumaImage2, (int)x-15, (int)y-15, (int)x+80, (int)y+80, imgX, imgY, imgX+s, imgY+s,null);
            }
            
        }
        else if(k>20 && k<=40){
            

            if(t==1 || t==2 || t==3 || t==4){
                //picDrawRight(g2);
                g2.drawImage(sirokumaImage, (int)x-5, (int)y-5, (int)x+90, (int)y+90, imgX, imgY, imgX+s, imgY+s,null);
            }
            else{
                //picDrawLeft(g2);
                g2.drawImage(sirokumaImage2, (int)x-5, (int)y-5, (int)x+90, (int)y+90, imgX, imgY, imgX+s, imgY+s,null);
            }
        }
        else if(k>40 && k<=60){
            if(t==1 || t==2 || t==3 || t==4){
                //picDrawRight(g2);
                g2.drawImage(sirokumaImage, (int)x-15, (int)y-15, (int)x+80, (int)y+80, imgX, imgY, imgX+s, imgY+s,null);
            }
            else{
                //picDrawLeft(g2);
                g2.drawImage(sirokumaImage2, (int)x-15, (int)y-15, (int)x+80, (int)y+80, imgX, imgY, imgX+s, imgY+s,null);
            }
        }

    }

    void angle(int t, Graphics2D g2){   //方向キーでアングルを調整
        AffineTransform at = g2.getTransform();
        switch (t) {
            case 0:     //上むき
                xAngle = x;
                yAngle = y-ds;
                
                //回転角度と画像の中心点を指定する
                at.setToRotation(Math.toRadians(180), x - 5, y - 5);
		        g2.setTransform(at);
                k = 0;
                break;
            case 1:     
                xAngle = x + dsr;
                yAngle = y - dsr;
                
                at.setToRotation(Math.toRadians(225), x - 5, y - 5);
		        g2.setTransform(at);
                k = 1;
                break;
            case 2:     //左向き
                xAngle = x + ds;
                yAngle = y;
                
                //xcAngle = xc + 1;
                //ycAngle = yc;
                at.setToRotation(Math.toRadians(270), x - 5, y - 5);
		        g2.setTransform(at);
                k = 2;
                break;
            case 3:
                xAngle = x + dsr;
                yAngle = y + dsr;
                
                //xcAngle = xc + 0.6;
                //ycAngle = yc + 0.6;
                at.setToRotation(Math.toRadians(315), x - 5, y - 5);
		        g2.setTransform(at);
                k = 3;
                break;
            case 4:     //下向き
                xAngle = x;
                yAngle = y + ds;
                
                //xcAngle = xc;
                //ycAngle = yc + 1;
                at.setToRotation(Math.toRadians(0), x - 5, y - 5);
		        g2.setTransform(at);
                k = 4;
                break;
            case 5:
                xAngle = x - dsr;
                yAngle = y + dsr;
                
                //xcAngle = xc - 0.6;
                //ycAngle = yc + 0.6;
                at.setToRotation(Math.toRadians(45), x - 5, y - 5);
		        g2.setTransform(at);
                k = 5;
                break;
            case 6:     //右向き
                xAngle = x - ds;
                yAngle = y;
                
                at.setToRotation(Math.toRadians(90), x - 5, y - 5);
		        g2.setTransform(at);
                k = 6;
                break;
            case 7:
                xAngle = x - dsr;
                yAngle = y - dsr;
                
                at.setToRotation(Math.toRadians(135), x - 5, y - 5);
		        g2.setTransform(at);
                k = 7;
                break;
            
            default:
                break;
        }
    }
}
