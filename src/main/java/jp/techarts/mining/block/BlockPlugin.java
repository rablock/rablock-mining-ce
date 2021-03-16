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

import java.io.IOException;
import java.util.List;
import jp.techarts.mining.Common;
import org.bson.Document;

public interface BlockPlugin {
  public Document createBlock(
      List<Document> poolList,
      String ip,
      String port,
      String server_name,
      String digest_username,
      String digest_pass,
      Common common)
      throws IOException;
}
