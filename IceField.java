
import java.awt.*;
import java.util.Random;

class IceField extends Canvas{
    double x, y, w, h;
    int t, tlock, r, r1, n, zWidth1, zWidth, zHeight, groundCheck, numMemo;
    int[] xPoints = new int[6];
    int[] yPoints = new int[6];
    double delta;
    int randIceNum = randomBreak(3);
    Image icePic1 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷六角形.png");
    Image icePic2 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷２.png");
    Image icePic3 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷３.png");
    Image icePicHibi1 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷六角形ヒビ.png");
    Image icePicHibi2 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷２ヒビ.png");
    Image icePicHibi3 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷３ヒビ.png");
    Image icePicHibi11 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷六角形ヒビ２.png");
    Image icePicHibi22 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷２ヒビ２.png");
    Image icePicHibi33 = getToolkit().getImage("/Users/furuyakensei/Pictures/氷３ヒビ2.png");
    IceField(int apWidth, int apHeight, int x, int y){
        this.x = x;
        this.y = y;
        //r = 56;
        r1 = apWidth*7;
        r = r1/120;
        n = 6;
        zWidth1 = r*6;
        zWidth = zWidth1/7;
        zHeight = (r*11)/56;
        

        delta = (360.0 / (double)n ) * ( Math.PI / 180.0);
        for(int i= 0; i<n; i++) {
			xPoints[i] = (int)(Math.cos(-Math.PI/2.0 + delta * i)*r) + x;
			yPoints[i] = (int)(Math.sin(-Math.PI/2.0 + delta * i)*r) + y;
		}
        /*for(int i=0; i<n; i++){
            xPoints[i] = xPoints[i] + (xPoints[i+1] - xPoints[i]);
        }*/
        w = 12;
        h = 12;
        t = 201; //氷が割れ始めてから沈む時間
        tlock = t;
    }

    void move(Graphics buf, int apWidth, int apHeight){
        
        buf.setColor(Color.black);
        if(this.t>0){
            //buf.drawPolygon(xPoints, yPoints, n);
            randIcePic(buf, 4);
            //buf.drawImage(icePic1, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
            //buf.drawLine((int)x-3, (int)y-3, (int)x+3, (int)y+3);
        }
    }
    boolean collisionCheck(HokkyokuBear obj) { 
        // 引数はホッキョクグマのオブジェクト 	
        if (Math.abs((this.x-obj.x)) <= (this.w+obj.w+30) && Math.abs(this.y-obj.y) <= (this.h+obj.h+30) && obj.jumpFlag==false) {
          
          return true;
        } 
        else{
          return false;
        }
        
      }

    int randomBreak(int num){
        Random n1 = new Random();
        return n1.nextInt(num);
        
    }

    public void randIcePic(Graphics buf, int n1){
            //int num = randomBreak(3);
            if(n1 == 4){
                numMemo = randIceNum;
            }
            else{numMemo = n1;}
        switch (numMemo) {
            case 0:
                if(this.t == this.tlock){
                    buf.drawImage(icePic1, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                else if(this.t < this.tlock && this.t >= this.tlock/2){
                    buf.drawImage(icePicHibi1, (int)this.x-50, (int)this.y-55, r*2-10, r*2, null);
                }
                else if(this.t < this.tlock/2 && this.t > 0){
                    buf.drawImage(icePicHibi11, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                    
                break;
            case 1:
                if(this.t == this.tlock){
                    buf.drawImage(icePic2, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                else if(this.t < this.tlock && this.t >= this.tlock/2){
                    buf.drawImage(icePicHibi2, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                else if(this.t < this.tlock/2 && this.t > 0){
                    buf.drawImage(icePicHibi22, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                break;
            case 2:
                if(this.t == this.tlock){
                    buf.drawImage(icePic3, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                else if(this.t < this.tlock && this.t >= this.tlock/2){
                    buf.drawImage(icePicHibi3, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                else if(this.t < this.tlock/2 && this.t > 0){
                    buf.drawImage(icePicHibi33, (int)this.x-50, (int)this.y-55, r*2-10, r*2+5, null);
                }
                break;
            default:
                break;
        }
    }

}