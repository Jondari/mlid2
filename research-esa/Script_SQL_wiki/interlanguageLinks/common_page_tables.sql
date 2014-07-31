-- table contenant le titre des pages communes entre le français, anglais et le chinois
CREATE TABLE common_page_fr (
	cp_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
        cp_title      varchar(255)
        );

CREATE TABLE common_page_en (
	cp_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
        cp_title      varchar(255)
        );


CREATE TABLE common_page_zh (
	cp_id int unsigned NOT NULL AUTO_INCREMENT PRIMARY KEY,
        cp_title      varchar(255)
        );


/* récupérer le titre des articles français communs au français anglais et chinois */
INSERT INTO common_page_fr
SELECT 0,sugg1.from_title FROM suggestions2 AS sugg1 INNER JOIN suggestions2 AS sugg2 ON sugg1.from_lang='fr' AND sugg1.to_lang='en' AND sugg1.from_title =sugg2.from_title AND sugg2.to_lang='zh';

/* récupérer le titre des articles anglais communs au français anglais et chinois */
INSERT INTO common_page_en
SELECT 0,sugg1.to_title FROM suggestions2 AS sugg1 INNER JOIN suggestions2 AS sugg2 ON sugg1.from_lang='fr' and sugg1.to_lang='en' AND sugg1.from_title =sugg2.from_title AND sugg2.to_lang='zh';

/* récupérer le titre des articles chinois communs au français anglais et chinois */
INSERT INTO common_page_zh
SELECT 0,sugg2.to_title FROM suggestions2 AS sugg1 INNER JOIN suggestions2 AS sugg2 ON sugg1.from_lang='fr' AND sugg1.to_lang='en' AND sugg1.from_title =sugg2.from_title AND sugg2.to_lang='zh';

