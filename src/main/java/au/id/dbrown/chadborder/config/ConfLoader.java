/**
 * 
 */
package au.id.dbrown.chadborder.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import au.id.dbrown.chadborder.ChadBorder.props;

/**
 * @author Damion Brown (dbrown) <the@dbrown.id.au> 0x7F1F27C3C19D9B74
 *
 */
public class ConfLoader {
	private static String file = "plugins/chadborder.conf";

	private Properties prop;

	public ConfLoader() {
		prop = new Properties();
	}

	public void setProp(props discordToken, String value) {
		prop.setProperty(discordToken.toString(), value);
	}

	public String getProp(props discordToken) {
		return prop.getProperty(discordToken.toString());
	}

	public void SaveProp() {
		OutputStream output = null;

		try {
			output = new FileOutputStream(file);
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void LoadProp() {
		InputStream input = null;

		try {
			input = new FileInputStream(file);
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns true if config exists.
	 */
	public boolean CheckProp() {
		File f = new File(file);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}
}
