-- Extract inter001language links into a single table for anaylysis
/*
SET NAMES utf8mb4;

CREATE DATABASE inter001 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

GRANT ALL ON inter001.* TO 'gio'@'localhost' IDENTIFIED BY 'wikiEsa';
*/

USE inter001;

-- Extract the information required, ie:
-- suggested reciporcal links
-- a:x -> b:x and b:x exists and no link from b:? to a:x exists => b:x -> a:x

INSERT INTO suggestions
SELECT  a.to_lang, a.to_title, a.from_lang, a.from_title
FROM inter a
INNER JOIN wiki_en.page p
    ON p.page_title = a.to_title
    -- AND p.page_namespace = 0
    AND p.page_is_redirect = 0
LEFT JOIN inter b
    ON   b.from_lang = a.to_lang
    AND  b.from_title = a.to_title     
    AND  b.to_lang = a.from_lang
WHERE a.from_lang IN ('en')
AND   a.to_lang = 'zh'
AND   b.from_lang IS NULL;

SELECT 'extration et ajout des éléments communs à la table suggestion terminé';





