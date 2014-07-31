INSERT INTO suggestions
SELECT a.to_lang, a.to_title, a.from_lang, a.from_title
FROM inter a
INNER JOIN wiki_fr.page p
    ON p.page_title = a.to_title
   -- AND p.page_namespace = 0 --
    AND p.page_is_redirect = 0
LEFT JOIN inter b
    ON   b.from_lang = a.to_lang
    AND  b.from_title = a.to_title     
    AND  b.to_lang = a.from_lang
WHERE a.from_lang IN ('zh' )
AND   a.to_lang = 'fr'
AND   b.from_lang IS NULL;
