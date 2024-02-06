
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import org.w3c.dom.events.MouseEvent;

public class EnGameMaster extends Canvas implements KeyListener {
    //###フィールド変数部分
    Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
    Graphics     buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
    Graphics    buf_kuma;
    Graphics    buf_kuma_jump;
    Dimension    d;     // アプレットの大きさを管理するオブジェクト
    private int  imgW, imgH; // キャンバスの大きさ
    private int mode = -1;
    private int angle;
    private int randBreakCount = 50;    //ランダムに氷が割れる時のカウント変数
    private int groundCheck = 0;    //海に落ちたらゲームオーバーを判定するための変数
    private int groCheckCounter = 10;   //海に落ちた判定を10回に一回行う
    private int noDieCount = 200;   //最初の無敵時間
    private int zWidth = imgW*(6/7)*(7/120);
    private int jumpCount = 0;      //ジャンプ時間の変数
    private int score = 0;      //スコアの変数
    private int scoreCounter= 0;        
    private boolean parentsFlag = false;       //子ぐまと親の当たり判定のフラグ
    private int childScoreCounter = 0;
    private int backScreen;
    Font customFont = new Font("Arial", Font.BOLD, 36); // フォント、スタイル、サイズ
    Font customFont1 = new Font("Arial", Font.BOLD, 24); // フォント、スタイル、サイズ
    HokkyokuBear hokkyokuBear;
    ChildBear childBear;
    //フィールドの氷を列ごとに宣言
    IceField r1Field[] = new IceField[3];
    IceField r2Field[] = new IceField[6];
    IceField r3Field[] = new IceField[7];
    IceField r4Field[] = new IceField[7];
    IceField r5Field[] = new IceField[7];
    IceField r6Field[] = new IceField[7];
    IceField r7Field[] = new IceField[6];
    IceField r8Field[] = new IceField[3];
    IceField randField1, randField2, randField3, randField4, randField5, randField6, randField7, randField8, childField;
    Image titleImage = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/istockphoto-1485787150-612x612-2.jpg");
    Image finishImage = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/istockphoto-1311275702-612x612.jpg");
    Image backImage = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/24544995.png");
    Image titlImage2 = getToolkit().getImage("/Users/furuyakensei/Pictures/bear-polar/Cool Text - All Sinks Down 451101255812104.png");


    //###コンストラクタ部分
    //キャンバスへの描画設定
    //ゲームの初期設定
    EnGameMaster(int imgW, int imgH){
        this.imgW = imgW;
        this.imgH = imgH;
        setSize(imgW, imgH);
        //addMouseListener(this);
        addKeyListener(this);
        backScreen = imgH;

        hokkyokuBear = new HokkyokuBear(imgW, imgH);
        for(int i = 0;i<r1Field.length;i++){        //フィールドは一列ずつ宣言
            r1Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+3), (imgH/10)*1);
        }
        for(int i = 0;i<r2Field.length;i++){
            r2Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+2)-r1Field[0].zWidth, (imgH/10)*2+r1Field[0].zHeight);
        }
        for(int i = 0;i<r3Field.length;i++){
            r3Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+1), (imgH/10)*3+(r1Field[0].zHeight*2));
        }
        for(int i = 0;i<r4Field.length;i++){
            r4Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+1)+r1Field[0].zWidth, (imgH/10)*4+(r1Field[0].zHeight*3));
        }
        for(int i = 0;i<r5Field.length;i++){
            r5Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+1), (imgH/10)*5+(r1Field[0].zHeight*4));
        }
        for(int i = 0;i<r6Field.length;i++){
            r6Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+1)+r1Field[0].zWidth, (imgH/10)*6+(r1Field[0].zHeight*5));
        }
        for(int i = 0;i<r7Field.length;i++){
            r7Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+2), (imgH/10)*7+(r1Field[0].zHeight*6));
        }
        for(int i = 0;i<r8Field.length;i++){
            r8Field[i] = new IceField(imgW, imgH, (imgW/10)*(i+3)-r1Field[0].zWidth, (imgH/10)*8+(r1Field[0].zHeight*7));
        }
        childField = r3Field[randomBreak(r3Field.length)];
        childBear = new ChildBear(imgW, imgH, childField);
        
    }

    //###メソッド部分

    //nullpointer exception回避
    public void addNotify(){
        super.addNotify();
        buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
        buf_gc = buf.getGraphics();
        buf_kuma = buf.getGraphics();
        buf_kuma_jump = buf.getGraphics();
    }

    //paintメソッド
    public void paint(Graphics g){
        buf_gc.setColor(Color.white);      // gc の色を白に
        buf_gc.fillRect(0, 0, imgW, imgH); // gc を使って白の四角を描く（背景の初期化）
        switch (mode) {
            case -1:     //ゲームタイトル画面  
                buf_gc.setColor(Color.blue); // タイトル画面を描く
                buf_gc.drawImage(titleImage, 0, 0, imgW, imgH, this);
                buf_gc.setFont(customFont);
                buf_gc.drawImage(titlImage2, 100, 0, this);
                //buf_gc.drawString(" ==  Game Title == ", imgW/2-80, imgH/2-20);
                buf_gc.drawString("Hit SPACE bar to start game", imgW/2-270, imgH-70);
                break;
            case -2:     //難易度選択
                buf_gc.setColor(Color.black);
                buf_gc.drawString("Select Game Level", imgW/2-80, imgH/2-20);
                break;
            case -3:     //ゲームオーバー画面（使ってない）
                buf_gc.setColor(Color.black);
                buf_gc.drawString("      == Game over ==      ", imgW/2-80, imgH/2-20);
                buf_gc.drawString("       Hit SPACE key       ", imgW/2-80, imgH/2+20);
                break;
            case -4:     //ゲームクリア画面
                buf_gc.setColor(Color.blue);
                buf_gc.drawImage(finishImage, 0, 0, imgW, imgH, this);
                buf_gc.setFont(customFont);
                buf_gc.drawString("YOUR SCORE   " + score, imgW/2+50, imgH/2-200);
                buf_gc.drawString(" == Game Finish !! ==", imgW/2-80, imgH/2-20);   //クリア画面
                //buf_gc.drawString("Nice Passion !!", imgW/2-80, imgH/2+20);
                break;
        
            case 1:
            //背景の移動処理
            buf_gc.drawImage(backImage, 0, backScreen, imgW ,imgH,this);
            buf_gc.drawImage(backImage, 0, backScreen - imgH , imgW,imgH,this);
            backScreen = backScreen - 1;
            if(backScreen < 0){
                backScreen = imgH;
            }

            //＃＃＃各オブジェクトの描画処理 and
            //###氷とホッキョクグマの接触チェック
            for(int i=0;i<r1Field.length; i++){
                if(r1Field[i].collisionCheck(hokkyokuBear)){
                    if(r1Field[i].t>0){
                        groundCheck++;
                        
                    }
                    if(r1Field[i].t==r1Field[1].tlock){
                        r1Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r1Field[i].t>=0 && r1Field[i].t<=r1Field[1].tlock-1)
                    {r1Field[i].t--;}
                //System.out.println("ll" + i + "は" + r1Field[i].t);
                r1Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r2Field.length; i++){
                if(r2Field[i].collisionCheck(hokkyokuBear)){
                    if(r2Field[i].t>0){
                        groundCheck++;
                        
                    }
                    if(r2Field[i].t==r1Field[1].tlock){
                        r2Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r2Field[i].t>=0 && r2Field[i].t<=r1Field[1].tlock-1)
                    {r2Field[i].t--;}
                    ////System.out.println(i + "は" + r2Field[i].t);
                r2Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r3Field.length; i++){
                if(r3Field[i].collisionCheck(hokkyokuBear)){
                    if(r3Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r3Field[i].t);
                    }
                    if(r3Field[i].t==r1Field[1].tlock){
                        r3Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r3Field[i].t>=0 && r3Field[i].t<=r1Field[1].tlock-1)
                    r3Field[i].t--;
                r3Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r4Field.length; i++){
                if(r4Field[i].collisionCheck(hokkyokuBear)){
                    if(r4Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r4Field[i].t);
                    }
                    if(r4Field[i].t==r1Field[1].tlock){
                        r4Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r4Field[i].t>=0 && r4Field[i].t<=r1Field[1].tlock-1)
                    r4Field[i].t--;
                r4Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r5Field.length; i++){
                if(r5Field[i].collisionCheck(hokkyokuBear)){
                    if(r5Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r5Field[i].t);
                    }
                    if(r5Field[i].t==r1Field[1].tlock){
                        r5Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r5Field[i].t>=0 && r5Field[i].t<=r1Field[1].tlock-1)
                    r5Field[i].t--;
                r5Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r6Field.length; i++){
                if(r6Field[i].collisionCheck(hokkyokuBear)){
                    if(r6Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r6Field[i].t);
                    }
                    if(r6Field[i].t==r1Field[1].tlock){
                        r6Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r6Field[i].t>=0 && r6Field[i].t<=r1Field[1].tlock-1)
                    r6Field[i].t--;
                r6Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r7Field.length; i++){
                if(r7Field[i].collisionCheck(hokkyokuBear)){
                    if(r7Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r7Field[i].t);
                    }
                    if(r7Field[i].t==r1Field[1].tlock){
                        r7Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r7Field[i].t>=0 && r7Field[i].t<=r1Field[1].tlock-1)
                    r7Field[i].t--;
                r7Field[i].move(buf_gc, imgW, imgH);
            }
            for(int i=0;i<r8Field.length; i++){
                if(r8Field[i].collisionCheck(hokkyokuBear)){
                    if(r8Field[i].t>0){
                        groundCheck++;
                        //System.out.println(i + "は" + r8Field[i].t);
                    }
                    if(r8Field[i].t==r1Field[1].tlock){
                        r8Field[i].t=r1Field[1].tlock-1;
                        groundCheck++;
                    }
                }
                if(r8Field[i].t>=0 && r8Field[i].t<=r1Field[1].tlock-1)
                    {r8Field[i].t--;}
                r8Field[i].move(buf_gc, imgW, imgH);
                //System.out.println("imgW" + imgW);
            }
            if(jumpCount==0){       //ジャンプモーションに入ってないなら
                hokkyokuBear.move(buf_kuma, imgW, imgH, hokkyokuBear.k);    //普通に歩く描画
            }
            if(childField.t >= 0 && parentsFlag==false){
                childBear.move(buf_gc);
            }

            if(childBear.collisionCheck(hokkyokuBear)){
                parentsFlag = true;
                
                if(childScoreCounter == 0){score = score + 500;}
                childScoreCounter++;
            }

            /*if(parentsFlag == true){
                childBear.x = hokkyokuBear.x;
                childBear.y = hokkyokuBear.y;
            }*/
            //buf_kuma.drawImage(sirokumaImage, (int)hokkyokuBear.x, (int)hokkyokuBear.y, 150, 150, this);
                //break;

            if(randBreakCount == 1 ){
                int b = randomBreak(8);
                switch (b) {
                    case 0:
                        randField1 = r1Field[randomBreak(r1Field.length)];
                        if(randField1.t == r1Field[1].tlock){randField1.t = r1Field[1].tlock-1;}
                        break;
                    case 1:
                        randField2 = r2Field[randomBreak(r2Field.length)];
                        if(randField2.t == r1Field[1].tlock){randField2.t = r1Field[1].tlock-1;}
                        break;
                    case 2:
                        randField3 = r3Field[randomBreak(r3Field.length)];
                        if(randField3.t == r1Field[1].tlock){randField3.t = r1Field[1].tlock-1;}
                        break;
                    case 3:
                        randField4 = r4Field[randomBreak(r4Field.length)];
                        if(randField4.t == r1Field[1].tlock){randField4.t = r1Field[1].tlock-1;}
                        break;
                    case 4:
                        randField5 = r5Field[randomBreak(r5Field.length)];
                        if(randField5.t == r1Field[1].tlock){randField5.t = r1Field[1].tlock-1;}
                        break;
                    case 6:
                        randField6 = r6Field[randomBreak(r6Field.length)];
                        if(randField6.t == r1Field[1].tlock){randField6.t = r1Field[1].tlock-1;}
                        break;
                    case 7:
                        randField7 = r7Field[randomBreak(r7Field.length)];
                        if(randField7.t == r1Field[1].tlock){randField7.t = r1Field[1].tlock-1;}
                        break;
                    case 8:
                        randField8 = r8Field[randomBreak(r8Field.length)];
                        if(randField8.t == r1Field[1].tlock){randField8.t = r1Field[1].tlock-1;}
                        break;
                    default:
                        break;
                }
            }
            //noDieCount--;
            if(randBreakCount>0){
                randBreakCount--;
            }
            else{
                randBreakCount = 50;
            }
            if(noDieCount>0){noDieCount--;}
            
            
            

            if(jumpCount >= 1){         //ジャンプ中なら
                groundCheck = 1;        //氷の上に乗っていることにする
                if(jumpCount%5==0){     //5回の描写に一回進める
                    hokkyokuBear.x = hokkyokuBear.xAngle;
                    hokkyokuBear.y = hokkyokuBear.yAngle;
                }
                hokkyokuBear.jump(buf_kuma_jump, hokkyokuBear.k, jumpCount);
                
                
                
                jumpCount++;
                if(jumpCount==60){      //ジャンプ動作が終わったら
                    hokkyokuBear.jumpFlag = false;
                    jumpCount=0;
                    
                }
            }

            if(groCheckCounter > 0){groCheckCounter--;}
            else{groCheckCounter = 20;}
            //System.out.println("noD" + noDieCount);
            if(groCheckCounter==1 && noDieCount==0){        //20描写に一回海に落ちたか判定
                System.out.println("gc" + groundCheck);
                if(groundCheck == 0){mode = -4;}
                else{groundCheck=0;}
            }
            groundCheck = 0;

            //スコア処理(左上に表示)
            scoreCounter++;
            if(scoreCounter%100==0){
                score = score + 100;
            }
            buf_gc.setFont(customFont1);
            buf_gc.drawString("SCORE" + score, 20, 20);
        }
        g.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける

    }
    public void update(Graphics gc) { // repaint() に呼ばれる
        paint(gc);
      }

    int randomBreak(int num){   //乱数生成の関数
        Random n1 = new Random();
        return n1.nextInt(num);
        
    }

    /*public void mouseClicked(MouseEvent e){}
    
    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mousePressed(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}*/

    public void keyPressed(KeyEvent e){
        int cd = e.getKeyCode();
        switch (cd) {
            case KeyEvent.VK_LEFT:      //8方向に分けて180度反対側からは方向転換ができない（真上の状態から矢印下を入力）
                System.out.println("left");
                if(hokkyokuBear.k == 0){
                    hokkyokuBear.k = 7;
                }
                else if(hokkyokuBear.k == 1 || hokkyokuBear.k == 7){
                    hokkyokuBear.k = hokkyokuBear.k - 1;
                }
                else if(hokkyokuBear.k == 3 ||hokkyokuBear.k == 4 || hokkyokuBear.k == 5){
                    hokkyokuBear.k = hokkyokuBear.k + 1;
                }
                else{
                    hokkyokuBear.k = hokkyokuBear.k;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(hokkyokuBear.k == 7){
                    hokkyokuBear.k = 0;
                }
                else if(hokkyokuBear.k == 0 || hokkyokuBear.k == 1 ){
                    hokkyokuBear.k = hokkyokuBear.k + 1;
                }
                else if(hokkyokuBear.k == 3 ||hokkyokuBear.k == 4 || hokkyokuBear.k == 5){
                    hokkyokuBear.k = hokkyokuBear.k - 1;
                }
                else{
                    hokkyokuBear.k = hokkyokuBear.k;
                }
                break;
            case KeyEvent.VK_UP:
                if(hokkyokuBear.k == 1 || hokkyokuBear.k == 2 || hokkyokuBear.k == 3){
                    hokkyokuBear.k = hokkyokuBear.k - 1;
                }
                else if(hokkyokuBear.k == 5 || hokkyokuBear.k == 6 ){
                    hokkyokuBear.k = hokkyokuBear.k + 1;
                }
                else if(hokkyokuBear.k == 7){
                    hokkyokuBear.k = 0;
                }
                else{
                    hokkyokuBear.k = hokkyokuBear.k;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(hokkyokuBear.k == 1 || hokkyokuBear.k == 2 || hokkyokuBear.k == 3){
                    hokkyokuBear.k = hokkyokuBear.k + 1;
                }
                else if(hokkyokuBear.k == 5 || hokkyokuBear.k == 6 || hokkyokuBear.k == 7){
                    hokkyokuBear.k = hokkyokuBear.k - 1;
                }
                else{
                    hokkyokuBear.k = hokkyokuBear.k;

                }
                break;
            case KeyEvent.VK_SPACE:
                
                System.out.println("space");
                if(mode==-1){mode=1;}
                break;
            case KeyEvent.VK_SHIFT:
                hokkyokuBear.x = hokkyokuBear.xAngle;
                hokkyokuBear.y = hokkyokuBear.yAngle;
                if(hokkyokuBear.cond1==0){
                    hokkyokuBear.cond1 = 1;
                }
                else if(hokkyokuBear.cond1==1){
                    hokkyokuBear.cond1 = -1;
                }
                else if(hokkyokuBear.cond1==-1){
                    hokkyokuBear.cond1 = 1;
                }
                break;
            case KeyEvent.VK_Z:     //ジャンプフラグ
                hokkyokuBear.jumpFlag = true;
                //hokkyokuBear.xc = hokkyokuBear.x;
                //hokkyokuBear.yc = hokkyokuBear.y;
                hokkyokuBear.jump(buf_kuma_jump, hokkyokuBear.k, jumpCount);
                if(jumpCount==0){jumpCount=1;}
                break;
        }
        
    }

    public void keyTyped(KeyEvent ke){
        int cd = ke.getKeyCode();
        String ss = "" + ke.getKeyChar();
        System.out.println(ss);
        
        repaint();
        
    }

    public void keyReleased(KeyEvent e){}
}
