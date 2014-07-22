package test.edu.kit.aifb.wikipedia.mlc;

import org.junit.Before;

import test.edu.kit.aifb.TestContextManager;
import edu.kit.aifb.wikipedia.mlc.MLCDatabase;

public class MLCDatabaseTest {

	MLCDatabase mlcDb;
	
	@Before
	public void loadMlcDatabase() {
		mlcDb = (MLCDatabase)TestContextManager.getContext().getBean(
				"wp200909_mlc_articles" );
	}
	
}
