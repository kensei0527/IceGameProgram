
import java.awt.*;

public class IceGame01 extends Frame implements Runnable{
    // ■ フィールド変数
    Thread     th;   // Thread クラスのオブジェクトを宣言
    EnGameMaster gm;   // ゲームの進行を担当するクラス

  //メイン関数
    public static void main(String[] args){
        new IceGame01();
    }

    //コンストラクタ
    IceGame01(){
        super("Shooting Game (Sample)"); // 親クラスのコンストラクタを呼び出す
        int cW=960, cH=720;      // キャンバスのサイズ
        //cW = 1500;
        //cH = 900;
        this.setSize(cW+30, cH+40);   // フレームのサイズを指定
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // キャンバスをフレームに配置

        gm = new EnGameMaster(cW,cH);// GameMaster クラスのオブジェクトを作成
        this.add(gm);              // キャンバスをフレームに配置
        this.setVisible(true);     // 可視化

        th = new Thread(this);     // Thread クラスのオブジェクトの作成
        th.start();                // スレッドを start メソッドで開始

        requestFocusInWindow();    // フォーカスを得る
  }

  // ■ メソッド (Runnable インターフェース用）
  public void run() {
    try {
      while (true) { // 無限ループ
	Thread.sleep(20); // ウィンドウを更新する前に指定時間だけ休止
	gm.repaint();     // 再描画を要求する． repaint() は update() を呼び出す
      }
    }
    catch (Exception e) {System.out.println("Exception: " + e);}
  }
    
}
