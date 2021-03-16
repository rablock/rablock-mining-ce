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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 共通処理の定義クラス<br>
 * Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
@Service
public class Common {

  private final Send send;

  @Autowired
  public Common(final Send send) {
    this.send = send;
  }
  /**
   * 現在時刻を取得する
   *
   * @return currentTime 現在時刻
   */
  public String getCurrentTime() {
    LocalDateTime localDataTime = LocalDateTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    return dtf.format(localDataTime);
  }

  /**
   * タイムスタンプを取得する
   *
   * @return Timestamp タイムスタンプ
   */
  public String getTimestamp() {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    return String.valueOf(timestamp.getTime());
  }

  /**
   * 文字列をハッシュ化する
   *
   * @param input ハッシュ化する文字列
   * @return result ハッシュ化した文字列
   * @throws NoSuchAlgorithmException ある暗号アルゴリズムが、現在の環境では使用可能でない場合に発生
   * @throws UnsupportedEncodingException 文字のエンコーディングがサポートされていない場合に発生
   */
  public String hashCal(String input)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(input.getBytes("utf8"));
    return String.format("%040x", new BigInteger(1, digest.digest()));
  }

  /**
   * poolのデータを取得する
   *
   * @return poolList poolのデータ
   * @throws IOException
   */
  public List<Document> getPoolList(
      String blockchain_ip, String blockchain_port, String digest_username, String digest_pass)
      throws IOException {
    final ObjectMapper mapper = new ObjectMapper();
    final String txdata =
        send.getInfo(blockchain_ip, blockchain_port, "deliveredpool", digest_username, digest_pass);
    final JsonNode data = mapper.readTree(txdata);
    return StreamSupport.stream(data.spliterator(), false)
        .map(current -> Document.parse(current.toString()))
        .collect(Collectors.toList());
  }

  /**
   * 最後のブロック取得
   *
   * @return lastHash 最後のブロック
   * @throws IOException
   */
  public JsonNode getLastBlock(
      String blockchain_ip, String blockchain_port, String digest_username, String digest_pass)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String lastBlock =
        send.getInfo(blockchain_ip, blockchain_port, "lastblock", digest_username, digest_pass);
    return mapper.readTree(lastBlock);
  }

  /**
   * 最後のブロックのハッシュ値取得
   *
   * @param blockchain_ip
   * @param blockchain_port
   * @return lastHash 最後のブロックのハッシュ値
   * @throws IOException
   */
  public String getLastHash(JsonNode lastBlock) throws IOException {
    return lastBlock.get("hash").asText();
  }

  /**
   * 最後のブロックのブロック高取得
   *
   * @param blockchain_ip
   * @param blockchain_port
   * @return lastHeight 最後のブロックのブロック高
   * @throws IOException
   */
  public int getLastHeight(JsonNode lastBlock) throws IOException {
    return lastBlock.get("height").asInt();
  }
}
