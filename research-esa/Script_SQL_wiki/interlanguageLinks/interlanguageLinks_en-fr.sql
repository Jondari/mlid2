-- Extract inter001language links into a single table for anaylysis
SET NAMES utf8mb4;

CREATE DATABASE inter001 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

GRANT ALL ON inter001.* TO 'gio'@'localhost' IDENTIFIED BY 'wikiEsa';

USE inter001;

CREATE TABLE inter (
     from_lang    varchar(10),
     from_title   varchar(255),
     to_lang      varbinary(20) NOT NULL default '',
     to_title     varchar(240) CHARACTER SET utf8mb4 binary NOT NULL
);

ALTER TABLE `inter` ENGINE = MYISAM; 

SELECT 'Création de la table inter terminé.'; 

USE wiki_en;

INSERT INTO inter001.inter
SELECT 'en', page.page_title, langlinks.ll_lang, langlinks.ll_title
FROM page, langlinks
WHERE page.page_id = langlinks.ll_from
AND   page.page_namespace = 0;

SELECT 'Ajout des éléments "en" à inter terminé.';

USE wiki_fr;

INSERT INTO inter001.inter
SELECT 'fr', page.page_title, langlinks.ll_lang, langlinks.ll_title
FROM page, langlinks
WHERE page.page_id = langlinks.ll_from
AND   page.page_namespace = 0;

SELECT 'Ajout des éléments "fr" à inter terminé.';

USE inter001;

-- Tidy up this table and index it:

UPDATE inter SET from_title = replace( from_title, '_', ' ' );
UPDATE inter SET to_title = replace( to_title, '_', ' ' );

-- Clear nonsensical entries
 -- entries to nonsensical languages
 -- entries to blank titles
 -- entries to unlikely titles
 -- poss entries to nonexistant articles ?

ALTER TABLE inter ADD INDEX ( from_lang, from_title );
ALTER TABLE inter ADD INDEX ( to_lang, to_title );

SELECT 'Ajout des indexes terminé.'; 

CREATE TABLE suggestions (
	id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
        from_lang       varchar(10),
        from_title      varchar(255),
        to_lang 	varbinary(20) NOT NULL default '',
	to_title        varchar(240) CHARACTER SET utf8mb4 binary NOT NULL
        );

ALTER TABLE `suggestions` ENGINE = MYISAM; 

SELECT 'Création de la table suggestion terminé.'; 

-- Extract the information required, ie:
-- suggested reciporcal links
-- a:x -> b:x and b:x exists and no link from b:? to a:x exists => b:x -> a:x

INSERT INTO suggestions
SELECT 0, a.to_lang, a.to_title, a.from_lang, a.from_title
FROM inter a
INNER JOIN wiki_fr.page p
    ON p.page_title = a.to_title
    -- AND p.page_namespace = 0
    AND p.page_is_redirect = 0
LEFT JOIN inter b
    ON   b.from_lang = a.to_lang
    AND  b.from_title = a.to_title     
    AND  b.to_lang = a.from_lang
WHERE a.from_lang IN ('en')
AND   a.to_lang = 'fr'
AND   b.from_lang IS NULL;

SELECT 'extration et ajout des éléments communs à la table suggestion terminé';

-- Deletion of duplicated links

DELETE suggestions FROM suggestions
LEFT OUTER JOIN (
        SELECT MIN(id) as id,from_lang,from_title,to_lang,to_title
        FROM suggestions
        GROUP BY from_lang,from_title,to_lang,to_title
    ) AS table_1
    ON suggestions.id = table_1.id
WHERE table_1.id IS NULL;
SELECT 'Suppression des doublons terminé';




