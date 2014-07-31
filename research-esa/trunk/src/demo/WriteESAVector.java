package demo;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.kit.aifb.ConfigurationException;
import edu.kit.aifb.ConfigurationManager;
import edu.kit.aifb.concept.IConceptDescription;
import edu.kit.aifb.concept.IConceptExtractor;
import edu.kit.aifb.concept.IConceptIndex;
import edu.kit.aifb.concept.IConceptIterator;
import edu.kit.aifb.concept.IConceptVector;
import edu.kit.aifb.document.TextDocument;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class WriteESAVector {

	static final String[] REQUIRED_PROPERTIES = { "dirSource", "dirDest",
	// "concept_index_bean",
	// "concept_description_bean",
	};

	static Log logger = LogFactory.getLog(WriteESAVector.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		try {
			// ApplicationContext context = new FileSystemXmlApplicationContext(
			// "*_context.xml");

			// Utilisation index anglais
			// ApplicationContext context = new FileSystemXmlApplicationContext(
			// "en_context.xml");

			// Utilisation index français
			ApplicationContext context = new FileSystemXmlApplicationContext(
					"fr_context.xml");

			ConfigurationManager confMan = (ConfigurationManager) context
					.getBean(ConfigurationManager.class);
			confMan.parseArgs(args);
			confMan.checkProperties(REQUIRED_PROPERTIES);
			Configuration config = confMan.getConfig();

			String conceptIndexBean = "default_concept_index";
			if (config.containsKey("concept_index_bean")) {
				conceptIndexBean = config.getString("concept_index_bean");
			}

			IConceptIndex index = (IConceptIndex) context
					.getBean(conceptIndexBean);
			logger.info("size of source index: " + index.size());
			IConceptExtractor esaExtractor = index.getConceptExtractor();

			String conceptDescriptionBean = "default_concept_description";
			if (config.containsKey("concept_description_bean")) {
				conceptDescriptionBean = config
						.getString("concept_description_bean");
			}
			IConceptDescription description = (IConceptDescription) context
					.getBean(conceptDescriptionBean);

			System.out.println("Le chemin vers le dossier source est : "
					+ config.getString("dirSource"));
			// on récupère le chein vers le dossier source
			String dirSource = config.getString("dirSource");

			String dirDest = config.getString("dirDest");

			String separator = System.getProperty("file.separator");

			// on récupère l'ensemble du contenu texte du dossier
			List<String> dirText = FileUtil.getListOfText(dirSource);

			// on récupère les noms de fichiers
			List<String> nameFile = FileUtil.getListNameFile(dirSource);

			int i = 0;
			for (String text : dirText) {
				String nameCrtFile = nameFile.get(i).substring(0,
						nameFile.get(i).length() - 4);
				System.out.println(nameCrtFile);
				TextDocument doc = new TextDocument(nameCrtFile);
				doc.setText("content", index.getLanguage(), text);

				logger.info("Computing ESA vector of: "
						+ doc.getText("content"));
				IConceptVector cv = esaExtractor.extract(doc);

				logger.info("Printing concept vector");
				IConceptIterator it = cv.orderedIterator();
				while (it.next()) {
					String d = description.getDescription(
							index.getConceptName(it.getId()),
							index.getLanguage());

					// String vectorLine = it.getValue() + "\t" + d + " ("
					// + index.getConceptName(it.getId()) + ")";
					String val = "" + it.getValue();

					String vectorLine = it.getValue() + " " + d + " ("
							+ index.getConceptName(it.getId()) + ")";

					System.out.println(vectorLine);

					String pathCol1 = dirDest + separator + "col1";
					File col1 = new File(pathCol1);
					if (!col1.exists()) {
						col1.mkdir();
					}

					FileUtil.writeText(dirDest + separator + nameCrtFile
							+ "Vector.txt", vectorLine, true);
					FileUtil.writeText(dirDest + separator + nameCrtFile
							+ "Vector.txt", "\n", true);

					FileUtil.writeText(pathCol1 + separator + nameCrtFile
							+ "Vector.txt", val, true);
					FileUtil.writeText(pathCol1 + separator + nameCrtFile
							+ "Vector.txt", "\n", true);
				}
				i++;
			}
		} catch (ConfigurationException e) {
			e.printUsage();
		}
	}
}
