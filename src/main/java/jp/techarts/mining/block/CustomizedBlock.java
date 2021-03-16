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


package jp.techarts.mining.block;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.List;
import jp.techarts.mining.Common;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ブロックの定義クラス<br>
 * Copyright (c) 2018 Technologic Arts Co.,Ltd.
 *
 * @author TA
 * @version 1.0
 */
@Component("CustomizedBlock")
public class CustomizedBlock implements BlockPlugin {
  private final Logger log = LoggerFactory.getLogger(CustomizedBlock.class);

  public Document createBlock(
      List<Document> poolList,
      String ip,
      String port,
      String server_name,
      String digest_username,
      String digest_pass,
      Common common)
      throws IOException {
    log.info("CustomizedBlock 使用");

    // 最後のブロック取得
    JsonNode lastBlock = common.getLastBlock(ip, port, digest_username, digest_pass);
    String lastHash = common.getLastHash(lastBlock);
    int lastHeight = common.getLastHeight(lastBlock);
    String os = getOS();

    return new Document()
        .append("height", lastHeight + 1)
        .append("size", poolList.size())
        .append("data", poolList)
        .append("settime", common.getCurrentTime())
        .append("timestamp", common.getTimestamp())
        .append("miner", server_name)
        .append("machine_os", os)
        .append("prev_hash", lastHash);
  }

  /**
   * OS名を取得する
   *
   * @return
   */
  private String getOS() {
    return System.getProperty("os.name").toLowerCase();
  }
}
