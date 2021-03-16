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

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTPユーザー認証処理クラス Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
public class HttpAuthenticator extends Authenticator {
  Logger log = LoggerFactory.getLogger(HttpAuthenticator.class);
  /** ユーザ名 */
  private final String username;
  /** パスワード */
  private final String password;

  /**
   * 指定されたユーザ情報でHTTPユーザー認証処理オブジェクトを構築します。
   *
   * @param username ユーザ名
   * @param password パスワード
   */
  public HttpAuthenticator(String username, String password) {
    if (username == null || password == null)
      throw new IllegalArgumentException("username or password is null.");

    this.username = username;
    this.password = password;
  }

  /**
   * 認証に使用するユーザ名を取得します。
   *
   * @return ユーザ名
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * 認証に使用するパスワードを取得します。
   *
   * @return パスワード
   */
  public String getPassword() {
    return this.password;
  }

  @Override
  protected PasswordAuthentication getPasswordAuthentication() {
    log.debug("RequestingHost\t:" + getRequestingHost());
    log.debug("RequestingSite\t:" + getRequestingSite());
    log.debug("RequestingPort\t:" + getRequestingPort());
    log.debug("RequestingProtocol\t:" + getRequestingProtocol());
    log.debug("RequestingPrompt\t:" + getRequestingPrompt());
    log.debug("RequestingScheme\t:" + getRequestingScheme());
    log.debug("RequestingURL\t:" + getRequestingURL());
    log.debug("RequestorType\t:" + getRequestorType());
    log.debug("UserName/PassWord\t:" + this.username + "/" + this.password);
    return new PasswordAuthentication(this.username, this.password.toCharArray());
  }
}
