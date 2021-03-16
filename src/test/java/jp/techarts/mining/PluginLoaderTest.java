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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jp.techarts.mining.block.BlockPlugin;
import jp.techarts.mining.block.BlockPluginConfig;
import jp.techarts.mining.block.BlockPluginFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = {BlockPluginConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PluginLoaderTest {
  @Autowired private BlockPluginFactory blockPluginFactory;

  @Test
  public void testGetSimpleBlock() {
    BlockPlugin simpleBlock = blockPluginFactory.getBlockPlugin("SimpleBlock");
    assertNotNull(simpleBlock);
  }

  @Test
  public void testGetCustomizedBlock() {
    BlockPlugin customizedBlock = blockPluginFactory.getBlockPlugin("CustomizedBlock");
    assertNotNull(customizedBlock);
  }
}
