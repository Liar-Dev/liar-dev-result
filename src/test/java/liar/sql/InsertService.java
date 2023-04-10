package liar.sql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

public class InsertService {

    private static final String FILE_PATH = "/home/koseyun/projects/spring/liargame/sql/";
    private static final String[] gameNames = new String[]{
            "오늘 게임 한판?",
            "라이어 게임 합시다.",
            "즐겜 ㄱㄱ",
            "라이어 고수만",
            "초보 환영",
            "초보자 환영",
            "라이어 게임 ㄱㄱ",
            "극 초보만"
    };


    public static void main(String[] args) {

        String memberFileName =  FILE_PATH + "insert_member.txt";

        int memberNum = 40000;
        String[] memberPk = new String[memberNum];
        String[] memberUserId = new String[memberNum];

        try {
            FileWriter writer = new FileWriter(memberFileName);
            BufferedWriter bf = new BufferedWriter(writer);

            bf.write("insert into member (member_id, user_id, username) values \n");

            for (int i = 0; i < memberNum; i++) {
                memberPk[i] = randomUUID().toString();
                memberUserId[i] = randomUUID().toString();
                String value = String.format("(%s, %s, %s)", memberPk[i], memberUserId[i], i);

                if (i != 0) {
                    bf.write(",\n");
                }
                bf.write(value);
            }
            bf.write(",\n");
            bf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        // topic
//        String topicFileName = FILE_PATH + "insert_topic.txt";
//        int topicNum = 10000;
//
//        try {
//            FileWriter writer = new FileWriter(topicFileName);
//            BufferedWriter bf = new BufferedWriter(writer);
//
//            bf.write("insert into topic (topic_id, topic_name) values \n");
//
//            for (int i = 0; i < topicNum; i++) {
//                String value = String.format("(%d, %s)", i + 1, i);
//
//                if (i != 0) {
//                    bf.write(",\n");
//                }
//                bf.write(value);
//            }
//            bf.write(",\n");
//            bf.close();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        String gameResultFileName = FILE_PATH + "insert_game_result.txt";
        int gameResultNum = 400000;

        String[] gameResultPk = new String[gameResultNum];
        String[] gameResultGameId = new String[gameResultNum];
        String winner;

        try {
            FileWriter writer = new FileWriter(gameResultFileName);
            BufferedWriter bf = new BufferedWriter(writer);

            bf.write("insert into game_result (game_result_id, created_at, modified_at, game_id, game_name, " +
                    "host_id, room_id, total_users, winner, topic_id) values \n");

            for (int i = 0; i < gameResultNum; i++) {

                gameResultPk[i] = randomUUID().toString();
                gameResultGameId[i] = randomUUID().toString();

                if (i % 2 == 0) winner = "LIAR";
                else winner = "CITIZEN";

                String value = String.format("(%s, %s, %s, %s, %s, %s, %s, %d, %s, %d)",
                        gameResultPk[i], now(), now(), gameResultGameId[i], gameNames[new Random().nextInt(7)],
                        memberUserId[new Random().nextInt(39000)], randomUUID(), 10,
                        winner, new Random().nextInt(1000) + 10);

                if (i != 0) {
                    bf.write(",\n");
                }

                bf.write(value);
            }
            bf.write(",\n");
            bf.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String playerResultFileName = FILE_PATH + "insert_player_result.txt";
//        int playerResultNum = 10;
        int playerResultNum = 49000000;

        String[] playerResultPk = new String[playerResultNum];

        try {
            FileWriter writer = new FileWriter(playerResultFileName);
            BufferedWriter bf = new BufferedWriter(writer);

            bf.write("insert into player_result (player_result_id, created_at, modified_at, answers, exp, " +
                    "game_role, is_win, game_result_id, member_id) values \n");

            int n = 0;
            int randomId;
            for (int i = 0; i < gameResultNum - 30; i++) {
//                for (int i = 0; i < 10; i++) {
              for (int j = 0; j < 10; j ++) {
                    playerResultPk[n] = randomUUID().toString();

                    if (i % 2 == 0) winner = "LIAR";
                    else winner = "CITIZEN";

                    randomId = new Random().nextInt(39000) + 1;

                    String value = String.format("(%s, %s, %s, %d, %d, " +
                                    "%s, %d, %s, %s)",
                            playerResultPk[n], now(), now(), i % 2 == 0 ? 0 : 1, new Random().nextInt(30),
                            winner, i % 2 == 0 ? 0 : 1, gameResultPk[i + 30], memberPk[randomId + j]);

                    value += ",\n";

                    bf.write(value);
                    n++;
                }
            }
            bf.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
