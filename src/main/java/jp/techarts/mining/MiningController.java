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

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import jp.techarts.mining.block.BlockPlugin;
import jp.techarts.mining.block.BlockPluginFactory;
import jp.techarts.mining.prop.GetAppProperties;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * コントローラークラス<br>
 * Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
@RestController
public class MiningController {
  private final Logger log = LoggerFactory.getLogger(MiningController.class);

  private BlockPlugin block;

  private final Common common;
  private final Send send;

  private final String serverName;
  private final String blockIpAddress;
  /** ブロックチェーンのポート番号 */
  private final String blockPort;
  /** ブロック生成時に使用するクラス名 */
  private final String blockClassName;

  private final String digestUserName;
  private final String digestPassword;

  private final BlockPluginFactory blockPluginFactory;

  @Autowired
  public MiningController(
      final GetAppProperties ip,
      final Common common,
      final Send send,
      final BlockPluginFactory blockPluginFactory) {
    this.common = common;
    this.send = send;
    this.blockPluginFactory = blockPluginFactory;
    serverName = ip.getServerName();
    blockIpAddress = ip.getBlockIpAddress();
    blockPort = ip.getBlockPort();
    blockClassName = ip.getBlockClassName();
    digestUserName = ip.getDigestUserName();
    digestPassword = ip.getDigestPassword();
  }
  /**
   * Mining
   *
   * @return
   * @throws IOException
   */
  @RequestMapping("/mining")
  @ApiOperation(value = "ブロックをマイニングする。", notes = "すべてのメソッドで同じ動作をする。")
  private String mining() throws MiningInternalException, IOException {
    try {
      block = blockPluginFactory.getBlockPlugin(blockClassName);
    } catch (Exception e) {
      throw new MiningInternalException("ブロックプラグイン読み込みエラー", e);
    }

    List<Document> poolList =
        common.getPoolList(blockIpAddress, blockPort, digestUserName, digestPassword);
    if (poolList.size() == 0) {
      return "OK";
    }

    Document blockObject = new Document();
    blockObject.putAll(
        block.createBlock(
            poolList,
            blockIpAddress,
            blockPort,
            serverName,
            digestUserName,
            digestPassword,
            common));

    // ハッシュ値を作成
    try {
      final String hashString = blockObject.toJson();
      final String hashValue = common.hashCal(hashString);
      blockObject = blockObject.append("hash", hashValue);
    } catch (NoSuchAlgorithmException e) {
      throw new MiningInternalException("システムエラー", e);
    }

    // ブロックチェーンに送る
    try {
      send.nodeSendBlock(blockObject, blockPort);
    } catch (IOException e) {
      log.warn("警告メッセージ  コード {}", 100);
      log.info("ブロックを送信できませんでした。");
      return "NG";
    }
    return "OK";
  }

  @ExceptionHandler(MiningInternalException.class)
  public String handleException(Exception e, HttpServletResponse response, Model model) {
    log.error(e.getMessage());
    model.addAttribute("errorMessage", e.getMessage());
    return "error";
  }
}
