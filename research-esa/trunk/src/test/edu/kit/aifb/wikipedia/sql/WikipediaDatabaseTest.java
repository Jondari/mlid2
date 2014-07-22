package test.edu.kit.aifb.wikipedia.sql;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import test.edu.kit.aifb.TestContextManager;
import edu.kit.aifb.wikipedia.sql.IPage;
import edu.kit.aifb.wikipedia.sql.Page;
import edu.kit.aifb.wikipedia.sql.PropertyNotInitializedException;
import edu.kit.aifb.wikipedia.sql.WikipediaDatabase;

public class WikipediaDatabaseTest {

	static WikipediaDatabase englishWp;
	static WikipediaDatabase germanWp;
	
	@BeforeClass
	public static void loadDatabase() {
		englishWp = (WikipediaDatabase)TestContextManager.getContext().getBean( "wp200909_database_en" );
		germanWp = (WikipediaDatabase)TestContextManager.getContext().getBean( "wp200909_database_de" );
	}
	
	@Test
	public void articleText() throws Exception {
		IPage p = englishWp.getArticle( "Mulled_wine" );
		Assert.assertEquals( "Mulled_wine", p.getTitle() );
		Assert.assertTrue( p.getId() >= 0 );
		
		String text = englishWp.getText( p );
		Assert.assertTrue( text.length() > 10 );
		Assert.assertTrue( text.substring( 100 ), text.contains( "red wine" ) );
		Assert.assertTrue( text.substring( 100 ), text.contains( "cinnamon" ) );
	}
	
	@Test
	public void specialCharacters() throws PropertyNotInitializedException {
		IPage p = germanWp.getArticle( "F�hre" );
		Assert.assertEquals( 16373, p.getId() );
		Assert.assertEquals( "F�hre", p.getTitle() );
		
		p = new Page( 16373 );
		germanWp.initializePage( p );
		Assert.assertEquals( "F�hre", p.getTitle() );
	}
	
}
