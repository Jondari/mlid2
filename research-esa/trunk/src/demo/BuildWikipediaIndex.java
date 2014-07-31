package demo;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.kit.aifb.ConfigurationException;
import edu.kit.aifb.ConfigurationManager;
import edu.kit.aifb.document.ICollection;
import edu.kit.aifb.nlp.Language;
import edu.kit.aifb.terrier.TerrierIndexFactory;

public class BuildWikipediaIndex {

	static Log logger = LogFactory.getLog(BuildWikipediaIndex.class);

	static final String[] REQUIRED_PROPERTIES = { "collection_bean",
			"language", "indexId", };

	static public void main(String[] args) throws Exception {
		try {
			// ApplicationContext context = new FileSystemXmlApplicationContext(
			// "*_context.xml" );

			String lang = args[1];

			ApplicationContext context = null;
			if (lang.contains("en")) {
				// Utilisation base de données commune anglaise
				System.out.println("using en_context;");
				context = new FileSystemXmlApplicationContext("en_context.xml");
			} else {

				// Utilisation base de données commune française
				System.out.println("using fr_context;");
				context = new FileSystemXmlApplicationContext("fr_context.xml");
			}

			ConfigurationManager confMan = (ConfigurationManager) context
					.getBean(ConfigurationManager.class);
			confMan.parseArgs(args);
			confMan.checkProperties(REQUIRED_PROPERTIES);
			Configuration config = confMan.getConfig();

			String indexId = config.getString("indexId");
			Language language = Language.getLanguage(config
					.getString("language"));

			ICollection collection = (ICollection) context.getBean(config
					.getString("collection_bean"));

			TerrierIndexFactory factory = (TerrierIndexFactory) context
					.getBean(TerrierIndexFactory.class);

			factory.buildIndex(indexId, language, collection);
		} catch (ConfigurationException e) {
			e.printUsage();
		}
	}

}
