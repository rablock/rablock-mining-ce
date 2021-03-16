/**
 * This file is part of Rablock Community Edition.
 *
 * Rablock Community Edition is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * Rablock Community Edition is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Rablock Community Edition.
 * If not, see <https://www.gnu.org/licenses/>.
 */


package jp.techarts.mining;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 他ノードにJSON形式のデータを送信し、結果を受け取るクラス<br>
 * Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
@Service
public class Send {
  private final Logger log = LoggerFactory.getLogger(Send.class);

  /**
   * 他ノードにブロックを送信する。
   *
   * @param obj 送信するデータ
   * @param blockchain_port ブロックチェーンのポート番号
   * @param send_ip 送信先IPアドレス
   */
  public void nodeSendBlock(Document obj, String blockchain_port) throws IOException {

    // 送信先URL
    String strPostUrl = "http://localhost:" + blockchain_port + "/checknode";
    // 登録するJSON文字列
    String block = obj.toJson();

    String method = "receiveSendBlock";

    String JSON =
        "{\"id\":\"1\",\"jsonrpc\":\"2.0\",\"method\":\""
            + method
            + "\",\"params\": {\"block\": "
            + block
            + "}}";
    // 認証
    callPost(strPostUrl, JSON);
  }

  /**
   * JSON文字列の送信
   *
   * @param strPostUrl 送信先URL
   * @param JSON 送信するJSON文字列
   * @return result 通信結果 成功：OK 失敗：NG
   * @throws IOException
   */
  public String callPost(String strPostUrl, String JSON) throws IOException {
    HttpURLConnection con = null;
    try {

      URL url = new URL(strPostUrl);

      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(1000);
      con.setConnectTimeout(1000);
      // HTTPリクエストコード
      con.setDoOutput(true);
      con.setRequestMethod("POST");
      con.setRequestProperty("Accept-Language", "jp");
      // データがJSONであること、エンコードを指定する
      con.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
      // POSTデータの長さを設定
      con.setRequestProperty("Content-Length", String.valueOf(JSON.length()));
      // リクエストのbodyにJSON文字列を書き込む
      OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
      out.write(JSON);
      out.flush();
      con.connect();

      // HTTPレスポンスコード
      final int status = con.getResponseCode();
      if (status == HttpURLConnection.HTTP_OK) {
        // 通信に成功した
        // テキストを取得する
        final InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        if (null == encoding) {
          encoding = "UTF-8";
        }
        try (final InputStreamReader inReader = new InputStreamReader(in, encoding);
            final BufferedReader bufReader = new BufferedReader(inReader)) {
          return bufReader.lines().collect(Collectors.joining());
        }
      } else {
        // 通信が失敗した場合のレスポンスコードを表示
        log.warn("警告メッセージ  通信エラーコード {}", status);
      }
    } finally {
      if (con != null) {
        // コネクションを切断
        con.disconnect();
      }
    }
    return "";
  }

  /**
   * ブロックチェーンアプリからデータを取得する
   *
   * @param String "deliveredpool": トランザクションデータ、"lastblock": 最後のブロック
   * @return
   * @throws IOException
   */
  String getInfo(
      String blockchain_ip,
      String blockchain_port,
      String param,
      String digest_username,
      String digest_pass)
      throws IOException {
    HttpURLConnection con = null;
    try {
      String apiUrl = "http://" + blockchain_ip + ":" + blockchain_port + "/get/" + param;
      URL connectUrl = new URL(apiUrl);
      // ユーザ認証情報の設定
      HttpAuthenticator httpAuth = new HttpAuthenticator(digest_username, digest_pass);
      Authenticator.setDefault(httpAuth);

      con = (HttpURLConnection) connectUrl.openConnection();

      con.setDoOutput(true);
      con.setInstanceFollowRedirects(true);

      int status = con.getResponseCode();
      if (status == HttpURLConnection.HTTP_OK) {
        // 通信に成功した
        // テキストを取得する
        final InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        if (null == encoding) {
          encoding = "UTF-8";
        }
        try (final InputStreamReader inReader = new InputStreamReader(in, encoding);
            final BufferedReader bufReader = new BufferedReader(inReader)) {
          return bufReader.lines().collect(Collectors.joining());
        }
      } else {
        // 通信が失敗した場合のレスポンスコードを表示
        log.warn("警告メッセージ  通信エラーコード {}", status);
      }
    } finally {
      if (con != null) {
        // コネクションを切断
        con.disconnect();
      }
    }
    return "";
  }
}
