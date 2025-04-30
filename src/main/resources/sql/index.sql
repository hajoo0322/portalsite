ALTER TABLE chatbot_faq
    ADD FULLTEXT INDEX ft_question (question) WITH PARSER ngram;