package demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.kit.aifb.ConfigurationManager;
import edu.kit.aifb.concept.ConceptVectorSimilarity;
import edu.kit.aifb.concept.IConceptExtractor;
import edu.kit.aifb.concept.IConceptIndex;
import edu.kit.aifb.concept.IConceptVector;
import edu.kit.aifb.concept.scorer.CosineScorer;
import edu.kit.aifb.document.TextDocument;
import edu.kit.aifb.nlp.Language;
import fr.inrialpes.exmo.mlid.util.Couple;
import fr.inrialpes.exmo.mlid.util.Couples;
import fr.inrialpes.exmo.mlid.util.FileUtil;

public class ComputeESASimilarity2 {

	static final String[] REQUIRED_PROPERTIES = {
			// "concept_index_bean",
			"language", "dir1", "dir2", "pathReport" };

	static Log logger = LogFactory.getLog(ComputeESASimilarity2.class);

	/**
	 * @param args
	 * @throws ConfigurationException
	 */
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext(
				"*_context.xml");
		ConfigurationManager confMan = (ConfigurationManager) context
				.getBean(ConfigurationManager.class);
		confMan.parseArgs(args);
		confMan.checkProperties(REQUIRED_PROPERTIES);
		Configuration config = confMan.getConfig();

		Language language = Language.getLanguage(config.getString("language"));

		String conceptIndexBean = "default_concept_index";
		if (config.containsKey("concept_index_bean")) {
			conceptIndexBean = config.getString("concept_index_bean");
		}

		IConceptIndex index = (IConceptIndex) context.getBean(conceptIndexBean);
		logger.info("size of source index: " + index.size());

		IConceptExtractor esaExtractor = index.getConceptExtractor();

		System.out.println("Le chemin vers le dossier 1 source est : "
				+ config.getString("dir1"));
		// on récupère le chemin vers les dossier 1, 2 et le rapport
		String dir1 = config.getString("dir1");

		String dir2 = config.getString("dir2");

		String pathReport = config.getString("pathReport");

		// on récupère l'ensemble du contenu texte du dossier
		List<String> dirText1 = FileUtil.getListOfText(dir1);

		// on récupère les noms de fichiers
		List<String> nameFile1 = FileUtil.getListNameFile(dir1);

		// on récupère l'ensemble du contenu texte du dossier
		List<String> dirText2 = FileUtil.getListOfText(dir2);

		// on récupère les noms de fichiers
		List<String> nameFile2 = FileUtil.getListNameFile(dir2);

		Couples<String> couplesName = new Couples<String>();

		Map<String, Double> map = new HashMap<String, Double>();

		int i = 0;
		int j = 0;
		for (String crtText1 : dirText1) {
			String crtNameFile1 = nameFile1.get(i).substring(0,
					nameFile1.get(i).length() - 4)
					+ "_d1";
			for (String crtText2 : dirText2) {
				String crtNameFile2 = nameFile2.get(j).substring(0,
						nameFile2.get(j).length() - 4)
						+ "_d2";

				if (!couplesName.exist(crtNameFile1, crtNameFile2)) {
					Couple<String> crtCpl = new Couple<String>(crtNameFile1,
							crtNameFile2);
					couplesName.add(crtCpl);

					TextDocument docA = new TextDocument(crtNameFile1);
					docA.setText("content", language, crtText1);

					TextDocument docB = new TextDocument(crtNameFile2);
					docB.setText("content", language, crtText2);

					logger.info("Computing ESA vector of: "
							+ docA.getText("content"));
					IConceptVector cvA = esaExtractor.extract(docA);

					logger.info("Computing ESA vector of: "
							+ docB.getText("content"));
					IConceptVector cvB = esaExtractor.extract(docB);

					ConceptVectorSimilarity sim = new ConceptVectorSimilarity(
							new CosineScorer());
					logger.info("Computing similarity");

					String key = crtNameFile1 + "/" + crtNameFile2;

					double value = sim.calcSimilarity(cvA, cvB);
					System.out.println("ESA Similarity: " + value);

					map.put(key, value);

				}
				j++;
			}
			i++;
		}

		Iterator iterator = map.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();

			String key = (String) entry.getKey();

			Double value = (Double) entry.getValue();
			String[] names = key.split("/");
			String name1 = names[0];
			String name2 = names[1];
			String phrase = name1 + " " + name2 + " " + value;
			FileUtil.writeText(pathReport, phrase + "\r", true);
		}
	}

}
