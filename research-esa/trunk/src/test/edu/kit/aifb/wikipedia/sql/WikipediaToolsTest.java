package test.edu.kit.aifb.wikipedia.sql;

import junit.framework.Assert;

import org.junit.Test;

import edu.kit.aifb.wikipedia.sql.WikipediaTools;

public class WikipediaToolsTest {

	@Test
	public void extractPlainText() {
		String text = "Bla \n{| Zeile 1\n|Content\n|}";
		Assert.assertEquals( "Bla \n  Zeile 1\n Content\n ", WikipediaTools.extractPlainText( text ) );
	}

}
