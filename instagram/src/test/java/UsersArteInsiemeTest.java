import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.giavacms.instagram.api.model.common.Media;
import org.giavacms.instagram.api.model.result.PaginationResult;
import org.giavacms.instagram.api.model.result.TagCountResult;
import org.giavacms.instagram.api.search.SearchMediaByTag;
import org.giavacms.instagram.api.search.SearchMediaCountByTag;


public class UsersArteInsiemeTest {
	private static String TOKEN = "5624011.6fd0d93.cc06ab2e8af94f37ace87092ec755c49";
	static String fileName = "/home/fiorenzo/Scrivania/content.txt";
	static String A_CAPO = "\n";

	public static void main(String[] args) throws Exception {
		boolean ancora = true;
		String maxNum = "";

		TagCountResult tagResult = SearchMediaCountByTag.execute(TOKEN,
				"arteinsieme", true);
		int i = 0;
		int numFoto = 0;
		Map<String, Long> users = new HashMap<String, Long>();
		while (ancora) {
			i++;
			PaginationResult result = SearchMediaByTag.execute(TOKEN, maxNum,
					"arteinsieme", true);
			if (result.getPagination().getNextMaxId() != null
					&& !result.getPagination().getNextMaxId().isEmpty()) {
				ancora = true;
				maxNum = result.getPagination().getNextMaxId();
			} else {
				ancora = false;
			}
			for (Media media : result.getData()) {
				numFoto++;
				if (users.containsKey(media.getUser().getUsername())) {
					Long countPhoto = users.get(media.getUser().getUsername());
					users.put(media.getUser().getUsername(), countPhoto + 1);
					System.out.println("VECCHIO:"
							+ media.getUser().getUsername());
				} else {
					users.put(media.getUser().getUsername(), 1L);
					System.out
							.println("NUOVO:" + media.getUser().getUsername());
				}
			}
		}
		int m = 0;
		scrivi(fileName, "num users: " + users.size() + A_CAPO);
		for (String user : users.keySet()) {
			m++;
			scrivi(fileName, m + ") " + user + ": " + users.get(user) + A_CAPO);
		}
	}

	public static void writeImage(String folder, String imgName, String imgExt,
			String imgURL) {
		try {
			URL url = new URL(imgURL);
			Image image = ImageIO.read(url);
			BufferedImage cpimg = bufferImage(image);
			File f1 = new File(folder + imgName + "." + imgExt);
			System.out.println("CREO IMG:" + f1.getAbsolutePath());
			ImageIO.write(cpimg, imgExt, f1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static BufferedImage bufferImage(Image image) {
		return bufferImage(image, BufferedImage.TYPE_INT_RGB);
	}

	public static BufferedImage bufferImage(Image image, int type) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), type);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, null, null);
		return bufferedImage;
	}

	public static void scrivi(String fileName, String content) throws Exception {

		File file = new File(fileName);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		// true = append file
		FileWriter fileWritter = new FileWriter(file.getAbsoluteFile(), true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		bufferWritter.write(content);
		bufferWritter.close();

		System.out.println("Done");
	}
}
