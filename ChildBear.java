import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class ChildBear extends Canvas{
    Image childPicture = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/bear-polar-2.png");
    int x, y, w, h;
    ChildBear(int apWidth, int apHeight, IceField obj){
        x = apWidth*2;
        y = apHeight*2;
        this.x = (int)obj.x;
        this.y = (int)obj.y;
        this.w = 50;
        this.h = 50;
    }

    void move(Graphics buf){
        buf.drawImage(childPicture, this.x-15, this.y-15, w, h, null);
    }

    boolean collisionCheck(HokkyokuBear obj){
        if (Math.abs((this.x-obj.x)) <= (this.w+obj.w) && Math.abs(this.y-obj.y) <= (this.h+obj.h) && obj.jumpFlag==false) {
            return true;
        } 
        else{
            return false;
        }
    }
}
