-- # select * from member;
-- SHOW VARIABLES LIKE 'secure_file_priv';
-- # #
-- # cp ./insert_member.txt /home/koseyun/projects/spring/liargame/sql/before_insert_member.txt
-- # cp ./insert_topic.txt /home/koseyun/projects/spring/liargame/sql/before_insert_topic.txt
-- # cp ./insert_game_result.txt /home/koseyun/projects/spring/liargame/sql/before_insert_game_result.txt
-- # cp ./insert_player_result.txt /home/koseyun/projects/spring/liargame/sql/before_insert_player_result.txt
--
--
-- # mv /home/koseyun/projects/spring/liargame/sql/insert_member.txt .
-- # mv /home/koseyun/projects/spring/liargame/sql/insert_topic.txt .
-- # mv /home/koseyun/projects/spring/liargame/sql/insert_game_result.txt .
-- # mv /home/koseyun/projects/spring/liargame/sql/insert_player_result.txt .
--
--
# LOAD DATA INFILE '/var/lib/mysql-files/insert_member.txt'
#     INTO TABLE member
#     FIELDS TERMINATED BY ', '
#     LINES starting by '(' TERMINATED BY '),\n'
#     IGNORE 1 LINES;






-- select count(*) from member;
--
-- select * from member limit 5;
--
-- LOAD DATA INFILE '/var/lib/mysql-files/insert_topic.txt'
--     INTO TABLE topic
--     FIELDS TERMINATED BY ', '
--     LINES starting by '(' TERMINATED BY '),\n'
--     IGNORE 1 LINES;
--
-- select count(*) from topic;
-- select * from topic limit 5;
--
-- LOAD DATA INFILE '/var/lib/mysql-files/insert_game_result.txt'
--     INTO TABLE game_result
--     FIELDS TERMINATED BY ', '
--     LINES starting by '(' TERMINATED BY '),\n'
--     IGNORE 1 LINES;
--
-- select count(*) from game_result;
-- select * from game_result limit 5;
--
-- LOAD DATA INFILE '/var/lib/mysql-files/insert_player_result.txt'
--     INTO TABLE player_result
--     FIELDS TERMINATED BY ', '
--     LINES starting by '(' TERMINATED BY '),\n'
--     IGNORE 1 LINES;
--
-- select * from player_result;

# member 1만
# player_result 100만

select user_id from member limit 1;

explain
select sql_no_cache * from player_result p
                               join member m on m.member_id = p.member_id
                               join game_result gr on p.game_result_id = gr.game_result_id
                               join topic t on gr.topic_id = t.topic_id
where m.user_id = '0003c0c8-4af1-4bd0-bdb8-62ceb8308fa5';


explain
select sql_no_cache * from player_result p
                               join game_result gr on p.game_result_id = gr.game_result_id
                               join member m on m.member_id = p.member_id
                               join topic t on gr.topic_id = t.topic_id
where m.user_id = '0003c0c8-4af1-4bd0-bdb8-62ceb8308fa5' and
        gr.game_name like '%ㄱㄱ%';

explain
select sql_no_cache * from player_result p
                               join game_result gr on p.game_result_id = gr.game_result_id
                               join member m on m.member_id = p.member_id
                               join topic t on gr.topic_id = t.topic_id
where m.user_id = '0003c0c8-4af1-4bd0-bdb8-62ceb8308fa5' and
          match (gr.game_name) against ('ㄱㄱ' in boolean mode);


# SET GLOBAL innodb_ft_aux_table = 'liardev/game_result';

SELECT WORD, DOC_COUNT, DOC_ID, POSITION
FROM INFORMATION_SCHEMA.INNODB_FT_INDEX_TABLE LIMIT 5;


explain
select * from member where user_id like'3%' limit 5;