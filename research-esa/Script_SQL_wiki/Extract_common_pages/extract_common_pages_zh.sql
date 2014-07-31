-- Spécifier quelle base d'intersection est utilisée
USE inter0002;

-- on ajoute les pages aux bases de données --
INSERT INTO wiki_common_zh.page
SELECT DISTINCT page_id,page_namespace,page_title,page_restrictions,page_counter,page_is_redirect, page_is_new,page_random,page_touched,page_latest,page_len  
FROM wiki_zh.page  
INNER JOIN common_page_zh ON page_title = cp_title;

SELECT 'Insertion des pages dans wiki_common_zh terminé';

-- On ajoute les révisions
INSERT INTO wiki_common_zh.revision
SELECT rev_id,rev_page,rev_text_id,rev_comment,rev_user,rev_user_text,
rev_timestamp,rev_minor_edit,rev_deleted,rev_len,rev_parent_id,rev_sha1
FROM wiki_zh.revision 
INNER JOIN wiki_common_zh.page where page_id = rev_page;

SELECT 'Insertion des pages dans wiki_common_zh terminé';

-- On ajoute les textes
INSERT INTO wiki_common_zh.text
SELECT old_id,old_text,old_flags 
FROM wiki_zh.text 
INNER JOIN wiki_common_zh.revision ON old_id=rev_text_id 
JOIN wiki_common_zh.page ON rev_page=page_id; 

SELECT 'Insertion des pages dans wiki_common_zh terminé';
