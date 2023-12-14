import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class DBtest{

    fun JDBC_TEST(money: Int){

        //JDBC 연결 정보
        val jdbcURL = "jdbc:mysql://localhost:3306/mysql"

        val user = "hitak" //or root(!= hitak)
        val password = "0000"

        //JDBC 드라이버 로드
        Class.forName("com.mysql.cj.jdbc.Driver")

        //데이터베이스 연결
        val connection: Connection = DriverManager.getConnection(jdbcURL, user, password)

        //데이터베이스에 데이터 추가
        insertData(connection, money)

        //데이터베이스에서 데이터 조회
        queryPlayerData(connection)

        //연결 닫기
        connection.close()

    }
    fun Table(connection: Connection){
        createPlayerTableIfNotExists(connection)
    }

    fun viewTable(connection: Connection){
        queryPlayerData(connection)
    }

    fun onlyOnePlayer(connection: Connection){
        val playerIdToKeep = 1 // 유지하고 싶은 플레이어의 ID
        deletePlayersExceptOne(connection, playerIdToKeep)
    }


    private fun createPlayerTableIfNotExists(connection: Connection) {
        val tableName = "Player"

        // 이미 존재하는지 확인
        if (isTableExists(connection, tableName)) {
            println("$tableName 테이블이 이미 존재합니다.")
            return
        }

        // 테이블 생성
        val createTableQuery = """
        CREATE TABLE $tableName (
            player_id INT AUTO_INCREMENT PRIMARY KEY,
            money INT DEFAULT 10000,
            profit INT,
            expenditure INT,
            free_recharge_count INT,
            play_round INT,
            game_purchase_count INT,
            first_prize_count INT,
            second_prize_count INT,
            third_prize_count INT,
            fourth_prize_count INT,
            fifth_prize_count INT,
            losing_count INT
        );
    """.trimIndent()

        // PreparedStatement 사용하여 SQL 쿼리 실행
        val createTableStatement: PreparedStatement = connection.prepareStatement(createTableQuery)
        createTableStatement.executeUpdate()

        println("$tableName 테이블이 생성되었습니다.")
    }

    private fun isTableExists(connection: Connection, tableName: String): Boolean {
        val checkTableQuery = "SHOW TABLES LIKE ?"
        val checkTableStatement: PreparedStatement = connection.prepareStatement(checkTableQuery)
        checkTableStatement.setString(1, tableName)
        val resultSet = checkTableStatement.executeQuery()

        return resultSet.next()
    }


    private fun insertData(connection: Connection, money: Int) {
        val insertQuery = "INSERT INTO Lotto (money) VALUES (?)"

        //PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(insertQuery)
        preparedStatement.setInt(1, money)
        preparedStatement.executeUpdate()

        println("Data inserted successfully.")
    }


    private fun queryPlayerData(connection: Connection) {
        val selectQuery = "SELECT * FROM Player"

        // PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        // 결과 출력
        while (resultSet.next()) {
            val playerId = resultSet.getInt("player_id")
            val money = resultSet.getInt("money")
            val profit = resultSet.getInt("profit")
            val expenditure = resultSet.getInt("expenditure")
            val freeRechargeCount = resultSet.getInt("free_recharge_count")
            val playRound = resultSet.getInt("play_round")
            val gamePurchaseCount = resultSet.getInt("game_purchase_count")
            val firstPrizeCount = resultSet.getInt("first_prize_count")
            val secondPrizeCount = resultSet.getInt("second_prize_count")
            val thirdPrizeCount = resultSet.getInt("third_prize_count")
            val fourthPrizeCount = resultSet.getInt("fourth_prize_count")
            val fifthPrizeCount = resultSet.getInt("fifth_prize_count")
            val losingCount = resultSet.getInt("losing_count")

            println(
                "Player ID: $playerId, Money: $money, Profit: $profit, Expenditure: $expenditure, " +
                        "Free Recharge Count: $freeRechargeCount, Play Round: $playRound, " +
                        "Game Purchase Count: $gamePurchaseCount " + "\n" +
                        "First Prize Count: $firstPrizeCount, " +
                        "Second Prize Count: $secondPrizeCount, Third Prize Count: $thirdPrizeCount, " +
                        "Fourth Prize Count: $fourthPrizeCount, Fifth Prize Count: $fifthPrizeCount, " +
                        "Losing Count: $losingCount"
            )
        }

        resultSet.close()
        preparedStatement.close()
    }


    private fun deletePlayersExceptOne(connection: Connection, playerIdToKeep: Int) {
        val deleteQuery = "DELETE FROM Player WHERE player_id <> ?"

        // PreparedStatement 사용하여 SQL 쿼리 실행
        val preparedStatement: PreparedStatement = connection.prepareStatement(deleteQuery)
        preparedStatement.setInt(1, playerIdToKeep)

        // 쿼리 실행
        preparedStatement.executeUpdate()

        preparedStatement.close()
    }

    fun initializePlayerData(connection: Connection){
        val updateQuery = """
            UPDATE Player 
            SET 
            money = 0, 
            profit = 0, 
            expenditure = 0, 
            free_recharge_count = 0, 
            play_round = 0, 
            game_purchase_count = 0, 
            first_prize_count = 0, 
            second_prize_count = 0, 
            third_prize_count = 0, 
            fourth_prize_count = 0, 
            fifth_prize_count = 0, 
            losing_count = 0
            """
        val preparedStatement: PreparedStatement = connection.prepareStatement(updateQuery)
        preparedStatement.executeUpdate()
        preparedStatement.close()

        println("플레이어 데이터 초기화")

    }

}