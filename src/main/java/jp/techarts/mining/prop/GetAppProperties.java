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


package jp.techarts.mining.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * propertyファイルの値を取得するクラス<br>
 * Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
@Component
public class GetAppProperties {

  /** 使用するポート番号 */
  @Value("${server.port}")
  private String port;

  /** 送信先のIPアドレス */
  @Value("${blockchain_ip.value}")
  private String blockIpAddress;

  /** 送信先のport番号 */
  @Value("${blockchain_port.value}")
  private String blockPort;

  /** サーバの名前 */
  @Value("${server_name.value}")
  private String server_name;

  /** ブロック生成時に使用するクラス名 */
  @Value("${block_class_name.value}")
  private String block_class_name;

  /** ダイジェスト認証 ユーザ名 */
  @Value("${digest.username}")
  private String digestUsername;

  /** ダイジェスト認証 パスワード */
  @Value("${digest.pass}")
  private String digestPass;

  public String getPort() {
    return port;
  }

  public String getBlockIpAddress() {
    return blockIpAddress;
  }

  public String getBlockPort() {
    return blockPort;
  }

  public String getServerName() {
    return server_name;
  }

  public String getBlockClassName() {
    return block_class_name;
  }

  public String getDigestUserName() {
    return digestUsername;
  }

  public String getDigestPassword() {
    return digestPass;
  }
}
