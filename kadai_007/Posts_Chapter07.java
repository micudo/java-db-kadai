package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		
		Connection con = null;
        PreparedStatement statement = null;
        Statement statement2 = null;
        
     // ユーザーリスト
        String[][] userList = {
            { "1003", "2023-02-08", "昨日の夜は徹夜でした・・", "13" },
            { "1002", "2023-02-08", "お疲れ様です！", "12"},
            { "1003", "2023-02-09" , "今日も頑張ります！", "18"},
            { "1001", "2023-02-09" , "無理は禁物ですよ！", "17"},
            { "1002", "2023-02-10" , "明日から連休ですね！", "20"}
        };

        try {
            // データベースに接続
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "iTry27@ud"
            );

            System.out.println("データベース接続成功");
            
         // SQLクエリを準備
            String sql = "INSERT INTO posts (user_id,posted_at,post_content,likes) VALUES (?, ?, ?, ?);";
            statement = con.prepareStatement(sql);

            // リストの1行目から順番に読み込む
            int rowCnt = 0;
            for( int i = 0; i < userList.length; i++ ) {
                // SQLクエリの「?」部分をリストのデータに置き換え
                statement.setInt(1, Integer.parseInt(userList[i][0])); // ユーザーID
                statement.setString(2, userList[i][1]); // 投稿日時
                statement.setString(3, userList[i][2]); // 投稿内容
                statement.setInt(4, Integer.parseInt(userList[i][3])); // いいね数

                // SQLクエリを実行（DBMSに送信）
                System.out.println("レコード追加:" + statement.toString() );
                rowCnt = statement.executeUpdate();
                System.out.println( rowCnt + "件のレコードが追加されました");
            }
            
         // SQLクエリを準備
            statement2 = con.createStatement();
            String sql2 = "SELECT posted_at,post_content,likes FROM posts where user_id = 1002;";

            //　SQLクエリを実行（DBMSに送信）
            ResultSet result = statement2.executeQuery(sql2);

            // SQLクエリの実行結果を抽出
            while(result.next()) {
                String posted_at = result.getString("posted_at");
                String post_content = result.getString("post_content");
                int likes = result.getInt("likes");
                System.out.println(result.getRow() + "件目：投稿日時=" + posted_at + "／投稿内容=" + post_content + "いいね数=" + likes);
            }
            
        } catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } finally {
            // 使用したオブジェクトを解放
            if( statement != null || statement2 != null ) {
                try { statement.close(); statement2.close();} catch(SQLException ignore) {}
            }
            if( con != null ) {
                try { con.close(); } catch(SQLException ignore) {}
            }
        }

	}

}
