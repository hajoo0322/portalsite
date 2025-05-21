ALTER TABLE chatbot_faq
    ADD FULLTEXT INDEX ft_question (question) WITH PARSER ngram;
--
-- ALTER TABLE news ADD FULLTEXT INDEX ft_news_title_desc (news_title, description),
--     ADD INDEX idx_news_created_at (created_at);
--
-- ALTER TABLE blog_post
--     ADD FULLTEXT INDEX ft_blog_title_desc (title, description),
--     ADD INDEX idx_blog_created_at (created_at);
--
-- ALTER TABLE cafe_post
--     ADD FULLTEXT INDEX ft_cafe_title_desc (title, description),
--     ADD INDEX idx_cafe_created_at (created_at);
--
-- ALTER TABLE member
--     ADD FULLTEXT INDEX ft_member_name (name);
--
-- ALTER TABLE cafe_member
--     ADD FULLTEXT INDEX ft_member_name (nickname);


-- 인덱싱 적용하실 분들은
-- 디스크C>ProgramData>MySQL>MySQL Server 8.4>my.ini 파일의
-- [mysqld] 하단에 innodb_ft_min_token_size=2 추가해주시고(관리자 권한으로 실행해야 변경 후 저장 가능),
--     >> 관리자로 실행 안 되면 메모장으로 켜서 Ctrl+O 눌러서 파일 찾고, 열기전에 우클릭 >> 속성 >> 보안 >> 편집 눌러서 컴퓨터 id별로 권한설정 가능
-- 그 다음에 작업 관리자>서비스 들어가서 MySQL84 '다시 실행' 해주신 다음에
-- 쿼리 콘솔에서 위의 주석처리된 쿼리들 실행해 주시면 됩니다.